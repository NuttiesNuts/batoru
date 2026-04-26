package net.sigma.batoru.rendering;

import java.util.*;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.item.custom.GauntletItem;
import net.sigma.batoru.rank.CombatRank;
import net.sigma.batoru.rank.RankUtil;
import org.joml.*;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class RankRenderPipeline implements ClientModInitializer {
    private static RankRenderPipeline instance;
    private static final RenderPipeline TEXTURED = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation("pipeline/batoru_rank_banner")
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .withCull(true)
                    .build()
    );

    private static final ByteBufferBuilder allocator = new ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE);
    private BufferBuilder buffer;

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f MODEL_OFFSET = new Vector3f();
    private static final Matrix4f TEXTURE_MATRIX = new Matrix4f();
    private MappableRingBuffer vertexBuffer;

    public static RankRenderPipeline getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        LevelRenderEvents.END_MAIN.register(this::extractAndDrawRank);
    }

    private static final Map<CombatRank, Identifier> rankTextures = new EnumMap<>(Map.of(
            CombatRank.F, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/f.png"),
            CombatRank.D, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/d.png"),
            CombatRank.C, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/c.png"),
            CombatRank.B, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/b.png"),
            CombatRank.A, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/a.png"),
            CombatRank.S, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/rank/s.png")
    ));

    private void extractAndDrawRank(LevelRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        PoseStack matrices = context.poseStack();
        Vec3 camera = context.levelState().cameraRenderState.pos;
        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        Map<CombatRank, List<AbstractClientPlayer>> playersByRank = new EnumMap<>(CombatRank.class);

        for (AbstractClientPlayer player : mc.level.players()) {
            TrinketAttachment trinkets = TrinketsApi.getAttachment(player);

            if (!trinkets.isEquipped(BatoruItems.GAUNTLET)) continue;

            var equipped = trinkets.getEquipped(BatoruItems.GAUNTLET);
            if (equipped.isEmpty()) continue;

            ItemStack gauntlet = equipped.getFirst().getB();
            CombatRank rank = RankUtil.getRank(gauntlet);

            if (rank == null) continue;
            playersByRank.computeIfAbsent(rank, r -> new ArrayList<>()).add(player);
        }

        if (playersByRank.isEmpty()) return;

        for (Map.Entry<CombatRank, List<AbstractClientPlayer>> entry : playersByRank.entrySet()) {
            CombatRank rank = entry.getKey();
            List<AbstractClientPlayer> players = entry.getValue();

            buffer = new BufferBuilder(allocator, TEXTURED.getVertexFormatMode(), TEXTURED.getVertexFormat());

            for (AbstractClientPlayer player : players) {
                double px = Mth.lerp(partialTick, player.xo, player.getX());
                double py = Mth.lerp(partialTick, player.yo, player.getY());
                double pz = Mth.lerp(partialTick, player.zo, player.getZ());

                matrices.pushPose();
                matrices.translate(px - camera.x, py - camera.y + 2.4, pz - camera.z);

                Quaternionf cameraOrientation = mc.getEntityRenderDispatcher().camera.rotation();
                Quaternionf yOnly = new Quaternionf(0, cameraOrientation.y, 0, cameraOrientation.w).normalize();
                matrices.mulPose(yOnly);

                renderFilledBox(matrices.last().pose(), buffer,
                        -0.5f, 0f, 0f, 0.5f, 1f, 0.01f,
                        1f, 1f, 1f, 1f); // white — texture provides the color

                matrices.popPose();
            }

            drawWithTexture(mc, TEXTURED, rankTextures.get(rank));
        }
    }

    private void drawWithTexture(Minecraft client, RenderPipeline pipeline, Identifier textureId) {
        MeshData builtBuffer = buffer.buildOrThrow();
        MeshData.DrawState drawParameters = builtBuffer.drawState();
        VertexFormat format = drawParameters.format();

        GpuBuffer vertices = upload(drawParameters, format, builtBuffer);

        AbstractTexture texture = client.getTextureManager().getTexture(textureId);

        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (pipeline.getVertexFormatMode() == VertexFormat.Mode.QUADS) {
            builtBuffer.sortQuads(allocator, RenderSystem.getProjectionType().vertexSorting());
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(builtBuffer.indexBuffer());
            indexType = builtBuffer.drawState().indexType();
        } else {
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.getVertexFormatMode());
            indices = shapeIndexBuffer.getBuffer(drawParameters.indexCount());
            indexType = shapeIndexBuffer.type();
        }

        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrix(), COLOR_MODULATOR, MODEL_OFFSET, TEXTURE_MATRIX);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> Batoru.MOD_ID + " rank render pipeline rendering",
                        client.getMainRenderTarget().getColorTextureView(), OptionalInt.empty(),
                        client.getMainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {

            renderPass.setPipeline(pipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);

            renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());

            renderPass.setVertexBuffer(0, vertices);
            renderPass.setIndexBuffer(indices, indexType);
            renderPass.drawIndexed(0, 0, drawParameters.indexCount(), 1);
        }

        builtBuffer.close();
        vertexBuffer.rotate();
        buffer = null;
    }

    private void renderFilledBox(Matrix4fc positionMatrix, BufferBuilder buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha) {
        // Front face
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setUv(0f, 1f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setUv(1f, 1f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setUv(1f, 0f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setUv(0f, 0f).setColor(red, green, blue, alpha);
        // Back face
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setUv(0f, 1f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, minY, minZ).setUv(1f, 1f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setUv(1f, 0f).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setUv(0f, 0f).setColor(red, green, blue, alpha);
    }

    private GpuBuffer upload(MeshData.DrawState drawParameters, VertexFormat format, MeshData builtBuffer) {
        // Calculate the size needed for the vertex buffer
        int vertexBufferSize = drawParameters.vertexCount() * format.getVertexSize();

        // Initialize or resize the vertex buffer as needed
        if (vertexBuffer == null || vertexBuffer.size() < vertexBufferSize) {
            if (vertexBuffer != null) {
                vertexBuffer.close();
            }

            vertexBuffer = new MappableRingBuffer(() -> Batoru.MOD_ID + " rank render pipeline", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE, vertexBufferSize);
        }

        // Copy vertex data into the vertex buffer
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

        try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(vertexBuffer.currentBuffer().slice(0, builtBuffer.vertexBuffer().remaining()), false, true)) {
            MemoryUtil.memCopy(builtBuffer.vertexBuffer(), mappedView.data());
        }

        return vertexBuffer.currentBuffer();
    }

    public void close() {
        allocator.close();

        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
    }
}

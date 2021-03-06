package it.polimi.ingsw.PSP14.client.view.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;

/**
 * Utility class with methods to create and handle 3D models in a 3D context.
 * It uses the flyweight pattern to minimally improve performances.
 */
public class ActorFactory {
    private static final double BLOCK_SCALE = 0.35;

    private static final Mesh
            BLOCK_1_MESH = getMesh("/assets/BuildingBlock01.obj"),
            BLOCK_2_MESH = getMesh("/assets/BuildingBlock02.obj"),
            BLOCK_3_MESH = getMesh("/assets/BuildingBlock03.obj"),
            DOME_MESH = getMesh("/assets/Dome.obj"),
            WORKER_M_MESH = getMesh("/assets/MaleBuilder.obj"),
            WORKER_F_MESH = getMesh("/assets/FemaleBuilder.obj");

    private static final PhongMaterial
            BLOCK_1_MAT = getMaterial("/assets/BuildingBlock01_v001.png"),
            BLOCK_2_MAT = getMaterial("/assets/BuildingBlock02_v001.png"),
            BLOCK_3_MAT = getMaterial("/assets/BuildingBlock03_v001.png"),
            DOME_MAT = getColor(Color.MIDNIGHTBLUE),
            WORKER_M_BLUE_MAT = getMaterial("/assets/MaleBuilder_Blue_v001.png"),
            WORKER_F_BLUE_MAT = getMaterial("/assets/FemaleBuilder_Blue_v001.png"),
            WORKER_M_ORANGE_MAT = getMaterial("/assets/MaleBuilder_Orange_v001.png"),
            WORKER_F_ORANGE_MAT = getMaterial("/assets/FemaleBuilder_Orange_v001.png"),
            WORKER_M_PINK_MAT = getMaterial("/assets/MaleBuilder_Pink_v001.png"),
            WORKER_F_PINK_MAT = getMaterial("/assets/FemaleBuilder_Pink_v001.png"),
            SELECTABLE_MAT = getMaterial("/assets/selectable2.png");

    static Mesh getMesh(String meshUrl) {
        // Import 3D resource
        URL modelUrl = ActorFactory.class.getResource(meshUrl);
        ObjModelImporter objImporter = new ObjModelImporter();

        objImporter.read(modelUrl);
        MeshView[] meshViews = objImporter.getImport();
        objImporter.close();

        // Get the first mesh (there should not be multiple meshes)
        MeshView meshView = meshViews[0];
        meshView.setCullFace(CullFace.BACK);

        return meshView.getMesh();
    }

    private static PhongMaterial getColor(Color color) {
        PhongMaterial texturedMaterial = new PhongMaterial();
        texturedMaterial.setDiffuseColor(color);
        return texturedMaterial;
    }

    private static PhongMaterial getMaterial(String textureUrl) {
        // This looks up to /resources/
        Image texture = new Image(textureUrl);
        PhongMaterial texturedMaterial = new PhongMaterial();
        texturedMaterial.setDiffuseMap(texture);

        return texturedMaterial;
    }

    /**
     * @param height tower height of the block
     * @return a meshview of a towerblock of a particular height
     */
    public static Node getBlock(int height) {
        MeshView block = new MeshView();
        switch (height) {
            case 0:
                block.setMesh(BLOCK_1_MESH);
                block.setMaterial(BLOCK_1_MAT);
                break;
            case 1:
                block.setMesh(BLOCK_2_MESH);
                block.setMaterial(BLOCK_2_MAT);
                break;
            case 2:
                block.setMesh(BLOCK_3_MESH);
                block.setMaterial(BLOCK_3_MAT);
                break;
            case 3:
                block.setMesh(DOME_MESH);
                block.setMaterial(DOME_MAT);
                break;
            default:
                throw new IndexOutOfBoundsException(height + " should be in range 0 - 3");
        }
        // Fill triangles
        block.setDrawMode(DrawMode.FILL);

        // Set position
        block.setTranslateX(0);
        block.setTranslateY(10);
        block.setTranslateZ(0);

        // Set scale (unified)
        block.setScaleX(BLOCK_SCALE);
        block.setScaleY(BLOCK_SCALE);
        block.setScaleZ(BLOCK_SCALE);

        return block;
    }

    /**
     * @param playerNumber id number of the player
     * @param workerNumber {0, 1} id number of the worker
     * @return a meshview of the worker of right gender and color
     */
    public static Node getWorker(int playerNumber, int workerNumber) {
        MeshView worker = new MeshView();
        // Assign Mesh
        switch (workerNumber) {
            case 0:
                worker.setMesh(WORKER_M_MESH);
                break;
            case 1:
                worker.setMesh(WORKER_F_MESH);
        }
        // Assign Texture
        switch (playerNumber) {
            case 0:
                worker.setMaterial(workerNumber == 0 ? WORKER_M_BLUE_MAT : WORKER_F_BLUE_MAT);
                break;
            case 1:
                worker.setMaterial(workerNumber == 0 ? WORKER_M_ORANGE_MAT : WORKER_F_ORANGE_MAT);
                break;
            case 2:
                worker.setMaterial(workerNumber == 0 ? WORKER_M_PINK_MAT : WORKER_F_PINK_MAT);
                break;
        }
        // Fill triangles
        worker.setDrawMode(DrawMode.FILL);

        // Set position
        worker.setTranslateX(0);
        worker.setTranslateY(10);
        worker.setTranslateZ(0);

        // Set scale (unified)
        worker.setScaleX(1);
        worker.setScaleY(1);
        worker.setScaleZ(1);

        return worker;
    }

    /**
     * @return a select box to be used to detect selection
     */
    public static Node getSelectable() {
        // Spawn a flat rectangle
        Box rect = new Box(2, 0.5, 2);
        // Set material
        // PhongMaterial mat = new PhongMaterial();
        // mat.setDiffuseColor(Color.valueOf("f9d854aa"));

        rect.setMaterial(SELECTABLE_MAT);
        // Animate it
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(rect.scaleXProperty(), 1),
                        new KeyValue(rect.scaleZProperty(), 1)

                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(rect.scaleXProperty(), 1.1),
                        new KeyValue(rect.scaleZProperty(), 1.1)
                ),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(rect.scaleXProperty(), 1),
                        new KeyValue(rect.scaleZProperty(), 1)

                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        return rect;
    }
}


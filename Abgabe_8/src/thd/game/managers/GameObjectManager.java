package thd.game.managers;

import thd.gameobjects.base.AutoMovable;
import thd.gameobjects.base.CollidableGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.MovableBackground;
import thd.gameobjects.movable.Rover;
import thd.gameview.GameView;

import java.util.ArrayList;
import java.util.LinkedList;

class GameObjectManager {
    private final LinkedList<GameObject> gameObjects;
    private final ArrayList<GameObject> toAdd;
    private final ArrayList<GameObject> toRemove;
    private final ArrayList<GameObject> toAddBackground;

    Rover rover;

    GameObjectManager(GameView gameView, GamePlayManager gamePlayManager) {
        gameObjects = new LinkedList<>();
        toAdd = new ArrayList<>();
        toAddBackground = new ArrayList<>();
        toRemove = new ArrayList<>();
        rover = new Rover(gameView, gamePlayManager);

    }

    void updateGameObjects() {
        modifyGameObjectsList();

        ArrayList<CollidableGameObject> collidables = new ArrayList<>(gameObjects.size());
        for (GameObject gameObject : gameObjects) {
            gameObject.updateStatus();
            if (gameObject instanceof AutoMovable) {
                ((AutoMovable) gameObject).updatePosition();
            }
            gameObject.addToCanvas();
            if (gameObject instanceof CollidableGameObject) {
                collidables.add((CollidableGameObject) gameObject);
            }
        }
        detectCollisionsAndNotifyGameObjects(collidables);
    }

    private void detectCollisionsAndNotifyGameObjects(ArrayList<CollidableGameObject> collidables) {
        for (int index = 0; index < collidables.size(); index++) {
            for (int other = index + 1; other < collidables.size(); other++) {
                if (collidables.get(index).collidesWith(collidables.get(other))) {
                    collidables.get(index).reactToCollision(collidables.get(other));
                    collidables.get(other).reactToCollision(collidables.get(index));
                }
            }
        }
    }

    /**
     * Adds object to gameObjects.
     *
     * @param gameObject is the object which gets added
     */
    void addGameObject(GameObject gameObject) {
        toAdd.add(gameObject);
    }

    /**
     * Removes object to gameObjects.
     *
     * @param gameObject is the object which gets removed
     */
    void removeGameObject(GameObject gameObject) {
        toRemove.add(gameObject);
    }

    void addBackgroundGameObject(GameObject gameObject) {
        toAddBackground.add(gameObject);
    }

    private void modifyGameObjectsList() {
        if (!toAddBackground.isEmpty()) {
            for (GameObject o : toAddBackground) {
                if (o.getClass() == MovableBackground.class) {
                    MovableBackground city = (MovableBackground) o;
                    gameObjects.add(city.gameObjectIndex, o);
                }else {
                    gameObjects.addFirst(o);
                }
            }
        }
        gameObjects.addAll(toAdd);
        gameObjects.removeAll(toRemove);
        toAddBackground.clear();
        toAdd.clear();
        toRemove.clear();

        if (gameObjects.size() > 2000) {
            throw new TooManyGameObjectsException("Too Many Objects");
        }
    }

    void moveWorld(double shiftX, double shiftY) {
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof AutoMovable)) {
                gameObject.worldHasMoved(shiftX, shiftY);
            }
        }
    }

    LinkedList<GameObject> getGameObjects() {
        return gameObjects;
    }
}

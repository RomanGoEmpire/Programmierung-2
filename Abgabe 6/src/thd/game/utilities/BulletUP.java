package thd.game.utilities;

import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.AutoMovable;
import thd.gameobjects.base.CollidableGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.movable.ufo.Ufo;
import thd.gameview.GameView;

import java.awt.*;

/**
 * BulletUp which gets shot by the Rover. It moves up. If it hits any Ufos or BulletDown they get destroyed.
 */

public class BulletUP extends CollidableGameObject implements AutoMovable {

    /**
     * Initializes the BulletUP.
     *
     * @param gameView        is the window it is displayed.
     * @param gamePlayManager is the manager, which makes sure that the objects are spawned and destroyed.
     * @param position        is the position the Bullet should spawn
     */
    public BulletUP(GameView gameView, GamePlayManager gamePlayManager, Position position) {
        super(gameView, gamePlayManager);
        this.position.x = position.x + 50;
        this.position.y = position.y;
        position.y += -20;
        position.x += 70;
        speedInPixel = 5;
    }

    @Override
    protected void initializeHitbox() {
        hitBoxWidth = 8;
        hitBoxHeight = 8;
        hitBoxOffsetX = 6;
        hitBoxOffsetY = 3;
    }

    @Override
    public void updateStatus() {
        if (position.y < 0) {
            gamePlayManager.destroy(this);
        }
    }

    @Override
    public void reactToCollision(CollidableGameObject other) {
        if (other.getClass() == Ufo.class) {
            gamePlayManager.destroy(this);
        }
    }

    @Override
    public void updatePosition() {
        position.up(speedInPixel);
    }

    @Override
    public void addToCanvas() {
        gameView.addTextToCanvas("*", position.x, position.y, 20, Color.green, 0);
    }
}


package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Projectile {

    // TODO:
    //  - Make a IHittable interface that Player and College inherits
    //  - Game can then perform checkHitHittable that returns either interface or null
    // TODO:
    //  - Call triggerHit on the IHittable returned when hit
    //  - This function should affect health / generate particles


    // Declare config, variables
    private static final Texture texture = new Texture(Gdx.files.internal("projectile.png"));
    private final float width = Game.PPT * 0.15f;
    private final float timeMax = 3f;
    private final float speed = Game.PPT * 2.5f;

    private Game game;
    IHittable source;
    private Sprite sprite;
    private Vector2 pos;
    private Vector2 vel;
    private boolean isFriendly;
    private float currentTime;
    private boolean toRemove;


    Projectile(Game game_, IHittable source_, Vector2 pos_, Vector2 vel_, boolean isFriendly_) {
        // Declare variables
        game = game_;
        source = source_;
        sprite = new Sprite(texture);
        pos = pos_;
        vel = vel_.nor();
        isFriendly = isFriendly_;
        currentTime = 0.0f;
        toRemove = false;

        // Setup sprite
        sprite.setPosition(pos.x, pos.y);
        sprite.setSize(width, width);
        sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
        sprite.setPosition(pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY());
    }


    public void update() {
        // Update position with velocity
        pos.x += vel.x * speed * Gdx.graphics.getDeltaTime();
        pos.y += vel.y * speed * Gdx.graphics.getDeltaTime();

        // Update sprite
        sprite.setPosition(pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY());

        // Update timer
        currentTime += Gdx.graphics.getDeltaTime();
        if (currentTime > timeMax) toRemove = true;

        // Check if hit anything
        Rectangle rect = getCollisionRect();
        IHittable hittableHit = game.checkHitHittable(rect);
        if (hittableHit != null) {
            if (isFriendly != hittableHit.getFriendly() && hittableHit != source) {

                // Hit something that is not friendly
                hittableHit.damage(10.0f);
                toRemove = true;
            }
        }
    }


    public void render(SpriteBatch batch) {
        // Draw sprite
        sprite.draw(batch);
    }


    public boolean shouldRemove() {
        // Return whether it should be removed
        return toRemove;
    }


    public Rectangle getCollisionRect() {
        // Calculate collision rectangle
        return new Rectangle(
            pos.x - width * 0.5f,
            pos.y - width * 0.5f,
            width, width
        );
    }
}

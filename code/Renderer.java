package root.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.HashMap;

import static root.content.Colors.HEALTH_BAR_COLOR;

public class Renderer {
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer renderer = new ShapeRenderer();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport = new ExtendViewport(16, 16, camera);
    private final Texture player = new Texture("player.png");
    private final HashMap<Field, Texture> textures = new HashMap<>();

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private Texture getTexture(Field field) {
        Texture texture = textures.get(field);
        if (texture == null) {
            texture = new Texture(field.getTextureName());
            textures.put(field, texture);
        }
        return texture;
    }

    private float smallest(float a, float b, float c, float d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    private float biggest(float a, float b, float c, float d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    public void draw(World world) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Position playerPosition = world.getPlayerPosition();
        boolean alive = world.isPlayerAlive();

        batch.begin();

        // Ermöglicht Konsistenz der Interpolationslogik bei schwankenden delta-Intervallen.
        float alpha = 1f - (float) Math.pow(10f, -Gdx.graphics.getDeltaTime());
        camera.position.lerp(new Vector3(playerPosition.x(), playerPosition.y(), 0), alpha);
        if (!alive) camera.zoom = MathUtils.lerp(camera.zoom, 3f, alpha);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Frustum frustum = camera.frustum;
        Vector3[] planePoints = frustum.planePoints;

        int left = (int) (smallest(planePoints[0].x, planePoints[1].x, planePoints[2].x, planePoints[3].x) - 0.5f);
        int right = (int) (biggest(planePoints[0].x, planePoints[1].x, planePoints[2].x, planePoints[3].x) + 0.5f);
        int bottom = (int) (smallest(planePoints[0].y, planePoints[1].y, planePoints[2].y, planePoints[3].y) - 0.5f);
        int top = (int) (biggest(planePoints[0].y, planePoints[1].y, planePoints[2].y, planePoints[3].y) + 0.5f);

        for (int y = bottom; y <= top; y++) {
            for (int x = left; x <= right; x++) {
                Field typ = world.getField(new Position(x, y));
                batch.setColor(typ.getColor());
                batch.draw(getTexture(typ), x - 0.5f, y - 0.5f, 1f, 1f);
            }
        }
        batch.setColor(Color.WHITE);
        batch.draw(player, playerPosition.x() - 0.5f, playerPosition.y() - 0.5f, 1f, 1f);
        batch.end();

        if (alive) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setProjectionMatrix(camera.combined);

            float width = world.getPoints() / 20f;
            renderer.setColor(HEALTH_BAR_COLOR);
            renderer.rect(playerPosition.x() - width / 2f, playerPosition.y() + 0.5f, width, .5f);

            renderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
}

package edu.lewisu.cs.hdondiego;

import com.badlogic.gdx.ApplicationAdapter;
import edu.lewisu.cs.cpsc41000.common.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DontFunkWithMe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture theKingMap;
	OrthographicCamera cam, titleCam;
	int WIDTH, HEIGHT;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		theKingMap = new Texture("the_king_map.png");
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(theKingMap, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		theKingMap.dispose();
	}
}

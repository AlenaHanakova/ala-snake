package com.alasnake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Alena Hanakova
 */
public enum SnakeCellType {

	HEAD, BODY, CORNER_LEFT, CORNER_RIGHT, TAIL;

	private Texture texture;
	private TextureRegion textureRegion;

	static {
		if (Gdx.files != null) {
			HEAD.texture = new Texture(Gdx.files.internal("images/head.png"));
			HEAD.textureRegion = new TextureRegion(HEAD.texture);
			BODY.texture = new Texture(Gdx.files.internal("images/body.png"));
			BODY.textureRegion = new TextureRegion(BODY.texture);
			CORNER_LEFT.texture = new Texture(Gdx.files.internal("images/cornerLeft.png"));
			CORNER_LEFT.textureRegion = new TextureRegion(CORNER_LEFT.texture);
			CORNER_RIGHT.texture = new Texture(Gdx.files.internal("images/cornerRight.png"));
			CORNER_RIGHT.textureRegion = new TextureRegion(CORNER_RIGHT.texture);
			TAIL.texture = new Texture(Gdx.files.internal("images/tail.png"));
			TAIL.textureRegion = new TextureRegion(TAIL.texture);
		}
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

}

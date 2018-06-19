package com.alasnake.screen;

import com.alasnake.game.Snake;
import com.alasnake.game.SpaceSnake;
import com.alasnake.game.Statistics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class FinishScreen implements Screen {

	private SpaceSnake game;

	private Stage stage;
	private final TextButton btnStartOver;

	private TextArea results;
	private int width;
	private int height;
	private int offset;
	private Statistics statistics;

	public FinishScreen(SpaceSnake game, Statistics statistics) {
		this.game = game;
		this.statistics = statistics;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();
		offset = width / 7;
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		Table screenTable = new Table(skin);
		screenTable.setSize(width, height);
		screenTable.setPosition(0, 0);
		screenTable.setBackground(new TextureRegionDrawable(new TextureRegion(SpaceSnake.backgroundTexture)));

		btnStartOver = new TextButton("Start Over", skin);
		btnStartOver.setSize(width / 5 + 2 * width / 9, height / 5);
		btnStartOver.setPosition(offset, height / 10);
		btnStartOver.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
				btnStartOver.setColor(1f, 0.4f, 0.4f, 1);
				btnStartClicked();
			}

			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
		});

		stage.addActor(btnStartOver);

		results = new TextArea("", skin);
		results.setPosition(width / 7, height / 3);
		results.setSize(width / 2, height / 2);
		results.setDisabled(true);
		List<String> userNames = statistics.getUserNames();
		int[] snakeSizes = statistics.getSnakeSizes();
		String a = "";
		for (int i = 0; i < userNames.size(); i++) {
			a += "Snake " + userNames.get(i);
			if (snakeSizes[i] > Snake.MINIMUM_LENGTH) {
				a += "\'s  size is " + snakeSizes[i] + "." + "\n";
			} else {
				a += " has died." + "\n";
			}
		}
		results.setText(a);
		screenTable.add(results).size(width / 2, height / 2).padBottom(height / 9);
		screenTable.row();
		screenTable.add(btnStartOver).size(width / 2, height / 4);
		stage.addActor(screenTable);
	}

	public void btnStartClicked() {
		game.setScreen(new LoginScreen(game));
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.getBatch().begin();
		stage.getBatch().draw(SpaceSnake.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.getBatch().end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
}

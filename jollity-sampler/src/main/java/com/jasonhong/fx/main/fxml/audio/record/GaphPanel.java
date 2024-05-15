package com.jasonhong.fx.main.fxml.audio.record;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javax.sound.sampled.*;


public   class GaphPanel extends StackPane implements Oscilloscope.OscilloscopeEventHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 4969781241442094359L;
	private static final int CANVAS_WIDTH = 800;
	private static final int CANVAS_HEIGHT = 100;

	float data[];
	GraphicsContext gc;
	Canvas canvas;
	public GaphPanel() {
//		setMinSize(80, 60);
		// 创建一个Canvas
		canvas=	 new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
		canvas.autosize();
		gc = canvas.getGraphicsContext2D();
		updateView();
		this.getChildren().add(canvas);

		// 启动波纹动画
//		new AnimationTimer() {
//			@Override
//			public void handle(long now) {
//				drawRipple();
//				phase += SPEED; // 更新相位
//			}
//		}.start();

	}private static final int WAVE_COUNT = 5; // 波纹数量
	private static final double AMPLITUDE = 30; // 振幅
	private static final double FREQUENCY = 2 * Math.PI / 200; // 频率（每200像素一个周期）
	private static final double SPEED = 0.02; // 波纹移动速度

	private double phase = 0; // 相位，用于控制波纹的位置

	private void updateView() {
		Platform.runLater(()->{
 // 设置宽度和高度
//		setGraphic(canvas);

		// 设置背景颜色（可选）
		gc.setFill(javafx.scene.paint.Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// 绘制线条（根据你的data数组）
		if (data != null) {
			float width = (float) canvas.getWidth();
			float height = (float) canvas.getHeight();
			float halfHeight = height / 2;

			for (int i = 0; i < data.length; i += 4) {
				gc.setStroke(javafx.scene.paint.Color.WHITE); // 设置线条颜色
				gc.strokeLine(data[i] * width, halfHeight - data[i + 1] * height,
						data[i + 2] * width, halfHeight - data[i + 3] * height);
			}
		}
		});
	}

	AudioDispatcher dispatcher;
	Mixer currentMixer;

	private void drawRipple() {
		Platform.runLater(()-> {
			gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT); // 清除画布


			// 绘制波纹
			for (int i = 0; i < WAVE_COUNT; i++) {
				double offsetX = i * CANVAS_WIDTH / (WAVE_COUNT - 1); // 每个波纹的偏移量
				for (int x = 0; x < CANVAS_WIDTH; x++) {
					double waveHeight = Math.sin((x - offsetX + phase) * FREQUENCY) * AMPLITUDE;
					if (waveHeight + CANVAS_HEIGHT / 2 > 0 && waveHeight + CANVAS_HEIGHT / 2 < CANVAS_HEIGHT) {
						// 只绘制在画布内的部分
						gc.strokeLine(x, CANVAS_HEIGHT / 2 + waveHeight, x + 1, CANVAS_HEIGHT / 2 + Math.sin((x + 1 - offsetX + phase) * FREQUENCY) * AMPLITUDE);
					}
				}
			}
		});
	}

	public void setNewMixer(AudioInputStream stream ) throws LineUnavailableException,
			UnsupportedAudioFileException {

		if(dispatcher!= null){
			dispatcher.stop();
		}

		int bufferSize = 2048;
		int overlap = 0;


		JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
		// create a new dispatcher
		dispatcher = new AudioDispatcher(audioStream, bufferSize,
				overlap);

		// add a processor, handle percussion event.
		//dispatcher.addAudioProcessor(new DelayEffect(400,0.3,sampleRate));
		dispatcher.addAudioProcessor(new Oscilloscope(this));
		//dispatcher.addAudioProcessor(new AudioPlayer(format));

		// run the dispatcher (on a new thread).
		new Thread(dispatcher,"Audio dispatching").start();
	}

	public void paint(float[] data, AudioEvent event) {
		this.data = data;
	}

	@Override
	public void handleEvent(float[] data, AudioEvent event) {
		paint(data, event);
//		repaint();
		updateView();
	}

	public void stop() {
		if(dispatcher!=null) {
			dispatcher.stop();
		}
	}
}
package org.fp024.j8ia.part03.chapter09;

interface Rotatable {
	void setRotationAngle(int angleInDegrees);

	int getRotationAngle();

	default void rotateBy(int angleInDegrees) {
		setRotationAngle((getRotationAngle() + angleInDegrees) % 360);
	}
}

interface Moveable {
	int getX();

	int getY();

	void setX(int x);

	void setY(int y);

	default void moveHorizontally(int distance) {
		setX(getX() + distance);
	}

	default void moveVertically(int distance) {
		setY(getY() + distance);
	}
}

interface Resizable {
	int getWidth();

	int getHeight();

	void setWidth(int width);

	void setHeight(int height);

	void setAbsoluteSize(int width, int height);

	default void setRelativeSize(int wFactor, int hFactor) {
		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
	}
}

/**
 * default 메서드는 구현을 반드시 할 필요가 없다.
 */
class Monster implements Rotatable, Moveable, Resizable {
	private int width;
	private int height;
	private int x;
	private int y;
	private int rotationAngle;

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;

	}

	@Override
	public void setAbsoluteSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setRotationAngle(int angleInDegrees) {
		this.rotationAngle = angleInDegrees;

	}

	@Override
	public int getRotationAngle() {
		return rotationAngle;
	}

}

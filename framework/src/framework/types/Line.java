package framework.types;


public class Line {
	
	/** Startpunkt der Linie */
	private Vector3D startPoint;
	
	/** Enpunkt der Linie */
	private Vector3D endPoint;
	
	private LineType lineType;
	
	public Line(Line line) {
		this(new Vector3D(line.getStartPoint()), new Vector3D(line.getEndPoint()), line.getLineType());
	}
	
	public Line(Vector3D startPoint, Vector3D endPoint) {
		this(startPoint, endPoint, LineType.CLOSE_LINE);
	}
	
	public Line(Vector3D startPoint, Vector3D endPoint, LineType lineType) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.lineType = lineType;
	}

	
	public Vector3D getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3D startPoint) {
		this.startPoint = startPoint;
	}

	public Vector3D getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector3D endPoint) {
		this.endPoint = endPoint;
	}	
	
	public LineType getLineType() {
		return lineType;
	}
	
	public boolean isOnLine(float s) {
		return lineType.isOnLine(s);
	}


	public enum LineType {
		CLOSE_LINE(0.0f, 1.0f),
		NEGATIVE_OPEN_LINE(Float.NEGATIVE_INFINITY, 0.0f),
		POSTIVE_OPEN_LINE(0.0f, Float.POSITIVE_INFINITY),
		OPEN_LINE(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
		
		float start;
		
		float end;
		
		private LineType(float start, float end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean isOnLine(float s) {
			return s > start && s < end;
		}
	}
}

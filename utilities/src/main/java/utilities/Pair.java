package utilities;


public class Pair<S, T> {
	
	private S firstElement;
	
	private T secondElement;
	
	
	public Pair(S firstElement, T secondElement) {
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}
	
	public S getFirst() {
		return firstElement;
	}
	
	public T getSecond() {
		return secondElement;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Pair<?, ?>) {
			return firstElement.equals(((Pair<?, ?>)object).firstElement) &&
				   secondElement.equals(((Pair<?, ?>)object).secondElement);
		}
		return false; 
	}
	
	@Override
	public int hashCode() {
		return (firstElement != null ? firstElement.hashCode() : 0) +
			   (secondElement != null ? secondElement.hashCode() : 0);
	}
	
	@Override
	public String toString() {
		return firstElement + " | " + secondElement;
	}
}

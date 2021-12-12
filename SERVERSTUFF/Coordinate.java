
/**
 * Represents a set of X, Y coordinates.
 * Allows coordinate sets to be directly compared to one another and sorted.
 */
public class Coordinate implements Comparable<Coordinate>
{
	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// column, row
	public int[] getCoordinate() {
		int[] coords = {x,y};
		return coords;
	}

	public int getCoordinate(char axis) {
		if(axis == 'x' || axis == 'X') {
			return x;
		}
		else if(axis == 'y' || axis == 'Y') {
			return y;
		}
		else {
			return -1;
		}
	}
	
	public boolean isRightOf(Coordinate otherCoord) {
		if(x > otherCoord.x) {
			return true;
		}
		return false;
	}
	
	public boolean isLeftOf(Coordinate otherCoord) {
		if(x < otherCoord.x) {
			return true;
		}
		return false;
	}
	
	public boolean isTooFarAway(Coordinate otherCoord) {
		if (Math.abs(y - otherCoord.y) > 1 || Math.abs(x - otherCoord.x) > 1) {
			return true;
		}
		return false;
	}
	
	public boolean isDiagonalAndAdjacentTo(Coordinate otherCoord) {
		if(Math.abs(otherCoord.y - y) != 1) {
			return false;
		}
		if(Math.abs(otherCoord.x - x) != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Implement Comparator interface, allowing
	 * coordinates to be sorted.
	 */
	public int compareTo(Coordinate c) {
		Coordinate c1 = this;
		Coordinate c2 = c;

		// NEG means this object is less than c2
		// 0 means this object is equal to c2
		// POS means this object is greater than c2
		
		// c1 is less than c2 if its y is less than c2's (vertical)
		if(c1.y < c2.y) {
			System.out.println(c1 + " is less than " + c2 + " :Y EVAL");
			return -1;
		}
		else if(c1.y > c2.y) {
			System.out.println(c1 + " is greater than " + c2 + " :Y EVAL");
			return 1;
		}
		
		// c1 is less than c2 if its x is less than c2's
		if(c1.x < c2.x) {
			System.out.println(c1 + " is less than " + c2 + " :X EVAL");
			return -1;
		}
		else if(c1.x > c2.x) {
			System.out.println(c1 + " is greater than " + c2 + " :X EVAL");
			return 1;
		}
		else {
			// They're exactly the same coordinate
			System.out.println(c1 + " is the same as " + c2 + " :X EVAL");
			return 0;
		}
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";	
	}
}
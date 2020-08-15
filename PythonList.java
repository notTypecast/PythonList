package pythonList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.DoubleFunction;
import java.util.function.ToIntFunction;
import java.util.Collections;


/**
 * Java implementation of the Python list
 * Includes methods that implement functionality offered by a regular Python list
 * Python list methods have been implemented with the same names
 * Since operator overloading isn't possible in Java, syntactic sugar functionality
 * has been implemented as methods
 * Similarly, functions that operate on Python lists have also been implemented as
 * methods
 * 
 * @author Nick Karydakis
 * @version 1.0
 * @since 2020-08-15
*/
public class PythonList implements Iterable<Object> {
	
	ArrayList<Object> arr;
	
	QuickSort sorter = null;
	
	private boolean numbers = false;
	private boolean strings = false;
	
	public PythonList(Object... items) {
		this.arr = new ArrayList<>();
		for (Object item: items) {
			this.arr.add(item);
		}
	}
	
	public <T> PythonList(Iterable<T> iterable) {
		this.arr = new ArrayList<>();
		for (T item: iterable) {
			this.arr.add(item);
		}
	}
	
	//Python list syntactic sugar (made into Java methods)
	
	public Object get(int i) {
		Integer actualIndex = getActualIndex(i, false);
		if (actualIndex == null) {
			throw new IndexError("IndexError: list index out of range");
		}
		return this.arr.get(actualIndex);
	}
	
	public PythonList slice(int start, int stop) {
		return this.slice(start, stop, 1);
	}
	
	public PythonList slice(int start, int stop, int step) {
		PythonList slicedList = new PythonList();
		
		start = this.getActualIndex(start, true);
		if (start == this.arr.size()) {
			--start;
		}
		stop = this.getActualIndex(stop, true);
		
		if ((start < stop && step < 0) || start > stop && step > 0) {
			return slicedList;
		}
		
		if (start < stop) {
		
			for (; start < stop; start += step) {
				slicedList.append(this.get(start));
			}
		
		}
		else {
			
			for (; start > stop; start += step) {
				slicedList.append(this.get(start));
			}
			
		}
		
		return slicedList;
	}
	
	public void update(int i, Object x) throws IndexError {
		try {
			this.arr.set(i, x);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexError("IndexError: list assignment index out of range");
		}
	}
	
	public void delete(int i) throws IndexError {
		try {
			this.arr.remove(i);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexError("IndexError: list assignment index out of range");
		}
	}
	
	public <T> PythonList add(Iterable<T> iterable) {
		PythonList newList = new PythonList(this);
		newList.extend(iterable);
		return newList;		
	}
	
	public PythonList times(int n) {
		PythonList newList = new PythonList();
		for (int i = 0; i < n; ++i) {
			newList.extend(this);
		}
		return newList;
	}
	
	public boolean contains(Object x) {
		return this.arr.contains(x);
	}
	
	//Python list methods
	
	public void append(Object x) {
		this.arr.add(x);
	}
	
	public void clear() {
		this.arr.clear();
	}
	
	public <T> void extend(Iterable<T> iterable) {
		for (Object obj: iterable) {
			this.arr.add(obj);
		}
	}
	
	public void insert(int i, Object x) {
		this.arr.add(this.getActualIndex(i, true), x);
		
	}
	
	public void remove(Object x) throws ValueError {
		if (!this.arr.remove(x)) {
			throw new ValueError("ValueError: list.remove(x): x not in list");
		}
	}
	
	public Object pop() throws IndexError {
		try {
			return this.pop(-1);
		} catch (IndexError e) {
			throw new IndexError("IndexError: pop from empty list");
		}
	}
	
	public Object pop(int i) throws IndexError {		
		Integer actualIndex = this.getActualIndex(i, false);
		if (actualIndex == null) {
			throw new IndexError("IndexError: pop index out of range");
		}
		Object removed = this.arr.get(actualIndex);
		this.arr.remove((int)actualIndex);
		return removed;
	}
	
	public int index(Object x) throws ValueError {
		return this.index(x, 0, this.arr.size());
	}
	
	public int index(Object x, int start) throws ValueError {
		return this.index(x, start, this.arr.size());
	}
	
	public int index(Object x, int start, int end) throws ValueError {
		
		try {
			if (start >= end) {
				throw new IndexOutOfBoundsException();
			}
			
			for (; start < end; ++start) {
				if (this.arr.get(start).equals(x)) {
					break;
				}
			}
			
			if (start == end) {
				throw new IndexOutOfBoundsException();
			}
			
			return start;
			
		} catch (IndexOutOfBoundsException e) {
			throw new ValueError("ValueError: " + x + " is not in list");
		}
		
		
	}
	
	public int count(Object x) {
		int occurrencies = 0;
		for (Object obj: this.arr) {
			if (obj.equals(x)) {
				++occurrencies;
			}
		}
		
		return occurrencies;
	}
	
	public void sort() throws ValueError {
		this.checkItemTypes();
		
		if (this.numbers) {
			this.sort((double d) -> {return d;}, false);
		}
		else if (this.strings) {
			this.alphabeticalSort();
		}
		else {
			throw new ValueError("ValueError: cannot sort list due to conflicting item types");
		}
	}
	
	public void sort(boolean reversed) throws ValueError {
		this.checkItemTypes();
		
		if (this.numbers) {
			this.sort((double d) -> {return d;}, reversed);
		}
		else if (this.strings) {
			this.alphabeticalSort();
		}
		else {
			throw new ValueError("ValueError: cannot sort list due to conflicting item types");
		}		
	}
	
	public void sort(DoubleFunction<Double> key) throws ValueError {
		this.sort(key, false);
	}
	
	public void sort(ToIntFunction<String> key) throws ValueError {
		this.sort(key, false);
	}
	
	public void sort(DoubleFunction<Double> key, boolean reversed) throws ValueError {
		if (this.arr.size() == 0) {
			return;
		}
			
		if (!this.numbers) {
			this.checkItemTypes();
		}
		
		if (this.numbers) {
			this.sorter = new QuickSort();
			
			double[] numArr = new double[this.arr.size()];
			ArrayList<Double> doubleList = this.toDoubleArrayList();
			for (int i = 0; i < numArr.length; ++i) {
				numArr[i] = doubleList.get(i);
			}
			
			this.sorter.sort(numArr, key);
			
			doubleList.clear();
			for (double d: numArr) {
				doubleList.add(d);
			}
			
			this.matchDoubleListToArr(doubleList);
		}	
		else {
			throw new ValueError("ValueError: Given function can only sort lists of numeric values");
		}
		
		if (reversed) {
			this.reverse();
		}
		
		this.numbers = false;
		
	}
	
	public void sort(ToIntFunction<String> key, boolean reversed) {
		if (this.arr.size() == 0) {
			return;
		}
		
		this.checkItemTypes();
		
		if (this.strings) {
			this.sorter = new QuickSort();
			
			String[] strArr = new String[this.arr.size()];
			strArr = this.toStringArrayList().toArray(strArr);
			
			this.sorter.sort(strArr, key);			
			
			this.arr.clear();
			
			for (String str: strArr) {
				this.arr.add((Object)str);
			}
		}
		else {
			throw new ValueError("ValueError: Given function can only sort lists of strings");
		}
		
		if (reversed) {
			this.reverse();
		}
		
		this.strings = false;
		
		
		
	}
	
	public void reverse() {
		Collections.reverse(this.arr);
	}	
	
	public PythonList copy() {
		PythonList copy = new PythonList();
		for (Object obj: this.arr) {
			copy.append(obj);
		}
		return copy;
	}
	
	//Python list functions (made into Java methods)
	
	public int length() {
		return this.arr.size();
	}
	
	public Object max() {
		this.checkItemTypes();
		
		if (this.numbers) {
			return this.max((double d) -> {return d;});
		}
		else if (this.strings) {
			return this.alphabeticalExtrema(true);
		}
		else {
			throw new ValueError("ValueError: cannot get list max due to conflicting item types");
		}
	}
	
	public Object max(DoubleFunction<Double> key) throws ValueError {
		if (!this.numbers) {
			this.checkItemTypes();
		}
		
		if (this.numbers) {
			FindExtrema finder = new FindExtrema();
			this.numbers = false;
			return this.matchDoubleToObject(finder.getMax(this.toDoubleArrayList(), key));
			
		}
		else {
			throw new ValueError("ValueError: max cannot be found due to conflicting item types");
		}
		
	}
	
	public Object max(ToIntFunction<String> key) throws ValueError {
		this.checkItemTypes();
		
		if (this.strings) {
			FindExtrema finder = new FindExtrema();
			this.strings = false;			
			return finder.getMax(this.toStringArrayList(), key);
		}
		else {
			throw new ValueError("ValueError: max cannot be found due to conflicting item types");
		}
	}
	
	public Object min() {
		this.checkItemTypes();
		
		if (this.numbers) {
			return this.min((double d) -> {return d;});
		}
		else if (this.strings) {
			return this.alphabeticalExtrema(false);
		}
		else {
			throw new ValueError("ValueError: cannot get list min due to conflicting item types");
		}
		
	}
	
	public Object min(DoubleFunction<Double> key) throws ValueError {
		if (!this.numbers) {
			this.checkItemTypes();
		}
		
		if (this.numbers) {
			FindExtrema finder = new FindExtrema();
			this.numbers = false;
			return this.matchDoubleToObject(finder.getMin(this.toDoubleArrayList(), key));
		}
		else {
			throw new ValueError("ValueError: max cannot be found due to conflicting item types");
		}
		
	}
	
	public Object min(ToIntFunction<String> key) throws ValueError {
		this.checkItemTypes();
		
		if (this.strings) {
			FindExtrema finder = new FindExtrema();
			this.strings = false;			
			return finder.getMin(this.toStringArrayList(), key);
		}
		else {
			throw new ValueError("ValueError: max cannot be found due to conflicting item types");
		}
	}
	

	//Special methods
	@Override
	public Iterator<Object> iterator() {
		return this.arr.iterator();
	}
	
	@Override
	public String toString() {
		String repr = "[";
		for(Object obj: this.arr) {
			repr += (obj instanceof String ? "\"" : "") + obj.toString() + (obj instanceof String ? "\", " : ", ");
		}
		if (repr.length() > 1) {
			repr = repr.substring(0, repr.length() - 2);
		}
		repr += "]";
		
		return repr;
	}
	
	//Private methods
	private Integer getActualIndex(int i, boolean fixOutOfBounds) {
		if (i >= 0) {
			if (i >= this.arr.size()) {
				if (fixOutOfBounds) return this.arr.size();
				else return null;
			}
			
			return i;
		}
		else {
			int pos = this.arr.size() + i;
			if (pos < 0 && !fixOutOfBounds) {
				return null;
			}
			return pos > 0 ? pos : 0;
		}
	}
	
	private void checkItemTypes() {
		if (this.arr.isEmpty()) {
			return;
		}		
		this.numbers = true;
		this.strings = true;
		for (Object obj: this.arr) {			
			if (!(obj instanceof Number)) {
				this.numbers = false;
			}
			if (!(obj instanceof String)) {
				this.strings = false;
			}
			
			if (!(this.strings || this.numbers)) {
				break;
			}
			
		}	
	}
	
	private void alphabeticalSort() {
		ArrayList<String> all = new ArrayList<>();
		for (Object str: this.arr) {
			all.add((String)str);
		}
		Collections.sort(all);
		this.arr.clear();
		for (String str: all) {
			this.arr.add((Object)str);
		}
	}
	
	private String alphabeticalExtrema(boolean max) {
		ArrayList<String> all = new ArrayList<>();
		for (Object str: this.arr) {
			all.add((String)str);
		}
		return max ? Collections.max(all) : Collections.min(all);
	}
	
	private ArrayList<Double> toDoubleArrayList() {
		ArrayList<Double> doubleList = new ArrayList<>();
		for (Object obj: this.arr) {
			if (obj instanceof Integer) {
				doubleList.add(((Integer)obj).doubleValue());
			}
			else {
				doubleList.add((Double)obj);
			}
		}
		return doubleList;		
	}
	
	private void matchDoubleListToArr(ArrayList<Double> doubleList) {
		for (int i = 0; i < doubleList.size(); ++i) {
			for (int j = 0; j < this.arr.size(); ++j) {
				boolean areEqual = false;
				if (arr.get(j) instanceof Integer) {
					areEqual = ((Integer)arr.get(j)).doubleValue() == doubleList.get(i);
				}
				else {
					areEqual = (Double)arr.get(j) == doubleList.get(i);
				}
				if (areEqual) {
					Collections.swap(this.arr, i, j);
				}
			}
		}
	}
	
	private ArrayList<String> toStringArrayList() {
		ArrayList<String> strList = new ArrayList<>();
		for (Object obj: this.arr) {
			strList.add((String)obj);
		}
		return strList;
	}
	
	private Object matchDoubleToObject(double d) {
		for (Object obj: this.arr) {
			if (obj instanceof Integer) {
				if (((Integer)obj).doubleValue() == d) {
					return obj;
				}
			}
			else {
				if (((Double)obj) == d) {
					return obj;
				}
			}
		}
		
		return null;
	}
		
	private class QuickSort {
		
		private double[] array;
		private DoubleFunction<Double> key;
		
		private String[] strArray;
		private ToIntFunction<String> strKey;
		
		public void sort(double[] inputArr, DoubleFunction<Double> key) {
			this.array = inputArr;
			this.key = key;
			quickSort(0, inputArr.length - 1);
		}
		
		public void sort(String[] inputArr, ToIntFunction<String> key) {
			this.strArray = inputArr;
			this.strKey = key;
			quickSort(0, inputArr.length - 1);
			
		}
		
		private void quickSort(int lowerIndex, int higherIndex) {
			
			int i = lowerIndex;
			int j = higherIndex;
			
			boolean doubleSort = this.array != null;
			
			double doublePivot = 0;
			int intPivot = 0;
			
			if (doubleSort) {
				doublePivot = this.key.apply(this.array[lowerIndex+(higherIndex-lowerIndex)/2]);
			}
			else {
				intPivot = this.strKey.applyAsInt(this.strArray[lowerIndex + (higherIndex - lowerIndex)/2]);
			}

			while (i <= j) {
				while (doubleSort ? this.key.apply(this.array[i]) < doublePivot : this.strKey.applyAsInt(this.strArray[i]) < intPivot) {
					++i;
				}
				while (doubleSort ? this.key.apply(this.array[j]) > doublePivot : this.strKey.applyAsInt(this.strArray[i]) < intPivot) {
					--j;
				}
				if (i <= j) {
					if (doubleSort) {
						this.doubleExchangeNumbers(i, j);
					}
					else {
						this.strExchangeNumbers(i, j);
					}
					++i;
					--j;
				}
			}
			if (lowerIndex < j)
				this.quickSort(lowerIndex, j);
			if (i < higherIndex)
				this.quickSort(i, higherIndex);
		}
		
		private void doubleExchangeNumbers(int i, int j) {
			double temp = this.array[i];
			this.array[i] = this.array[j];
			this.array[j] = temp;
		}
		
		private void strExchangeNumbers(int i, int j) {
			String temp = this.strArray[i];
			this.strArray[i] = this.strArray[j];
			this.strArray[j] = temp;
		}
	     
	}
	
	private class FindExtrema {
		private ArrayList<Double> array;
		private DoubleFunction<Double> key;
		
		private ArrayList<String> strArray;
		private ToIntFunction<String> strKey;
		
		public double getMax(ArrayList<Double> array, DoubleFunction<Double> key) {
			this.array = array;
			this.key = key;
			return this.forDoubles(true);
		}
		
		public double getMin(ArrayList<Double> array, DoubleFunction<Double> key) {
			this.array = array;
			this.key = key;
			return this.forDoubles(false);
		}
		
		public String getMax(ArrayList<String> array, ToIntFunction<String> key) {
			this.strArray = array;
			this.strKey = key;
			return this.forStrings(true);
		}
		
		public String getMin(ArrayList<String> array, ToIntFunction<String> key) {
			this.strArray = array;
			this.strKey = key;
			return this.forStrings(false);
		}
		
		private double forDoubles(boolean max) {
			double result = this.array.get(0);
			double resultWithFunc = this.key.apply(result);
			
			for (int i = 1; i < this.array.size(); ++i) {
				double curr = this.array.get(i);
				double currWithFunc = this.key.apply(curr);
				if (max ? currWithFunc > resultWithFunc : currWithFunc < resultWithFunc) {
					result = curr;
					resultWithFunc = currWithFunc;
				}
			}
			
			return result;		
		}
		
		private String forStrings(boolean max) {
			String result = this.strArray.get(0);
			int resultWithFunc = this.strKey.applyAsInt(result);
			
			for (int i = 1; i < this.strArray.size(); ++i) {
				String curr = this.strArray.get(i);
				int currWithFunc = this.strKey.applyAsInt(curr);
				if (max ? currWithFunc > resultWithFunc : currWithFunc < resultWithFunc) {
					result = curr;
					resultWithFunc = currWithFunc;
				}
			}
			
			return result;	
		}
		
		
	}
	

}

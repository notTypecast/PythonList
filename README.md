# PythonList
Python List implementation for Java

* Constructing a new list 
1) `PythonList l = new PythonList();`
2) `PythonList l = new PythonList("a", 4, new ArrayList<String>(), new PythonList());`
3) `PythonList l2 = new PythonList(l);`

The first will create a new, empty PythonList. The second will create a new PythonList and add the passed elements to it.
The third one, assuming `l` is any iterable (including a PythonList), will create a new PythonList and copy all the elements
of the passed iterable to it.
* PythonList methods implementing syntactic sugar
1) `list.get(int i)` returns the element at the provided index, `i`. Returns an object of type `Object`, which should be cast
to the appropriate type. Is equivalent to Python's `l[i]`.
2) `list.slice(int start[, int stop[, int step]])` returns a new PythonList object, representing a slice of the initial list.
`start` represents the initial index and is inclusive. `stop` represents the final index and is exclusive. `step` represents
the total items to be skipped and is 1 by default. Any of the three arguments can be negative. For `start` and `stop`, negative
values represent indexes counting backwards (from the end of the list). A negative step simply means to go backwards instead of
forwards. The method cannot throw any exceptions, but returns an empty list whenever an incorrect slice is passed. Is equivalent
to Python's `l[start:stop:step]`.
3) `list.update(int i, Object x)` updates the item at the given position, `i`, with the passed object, `x`. If an incorrect 
index is given,`IndexError` is thrown. Has no return value. Is equivalent to Python's `l[i] = x`.
4) `list.delete(int i)` removes the item at the given position, `i`, from the list. Similarly to `update`, if an incorrect 
index is given, `IndexError` is thrown. Has no return value. Is equivalent to Python's `del l[i]`.
5) `list.add(Iterable<T> iterable)` returns a new PythonList, containing both the items of `list`, as well as those of the
passed iterable. `list` itself is not affected. Is equivalent to Python's `l + l2`.
6) `list.times(int n)` returns a new PythonList, which represents `list` multiplied by `n`. Is equivalent to Python's `l * n`.
7) `list.contains(Object x)` returns a boolean value, representing whether `x` is included in `list`. Is equivalent to Python's
`x in l`.
* PythonList methods equivalent to actual python list methods
8) `list.append(Object x)` adds `x` to `list`. Has no return value.
9) `list.clear()` removes all items from `list`. Has no return value.
10) `list.extend(Iterable<T> iterable)` adds every item of the passed iterable to `list`. Has no return value.
11) `list.insert(int i, Object x)` inserts object `x` at the given index, `i`. An error is never thrown; if `i` is equal to, or
larger than the length of the list, `x` is added to the end. If `i` is negative, counting starts from the end of the list. Has no
return value.
12) `list.remove(Object x)` removes `x` from `list`. If `x` is not in `list`, `ValueError` is thrown. Has no return value.
13) `list.pop([int i])` removes the item at index `i` from the list and returns it. If no index is provided, the last item is 
removed.
14) `list.index(Object x[, int start[, int end]])` returns the index of `x` in the list. `start` and `end` represent slices, with
`start` being inclusive and `end` being exclusive. If `x` is not in `list`, `ValueError` is thrown.
15) `list.count(Object x)` returns total occurrencies of `x` in `list`.
16) `list.sort([DoubleFunction<Double> key / ToIntFunction<String> key][, boolean reversed])` sorts `list`. Sorting will only work if
either all items in `list` are numbers, or if all items in `list` are strings. `reversed` can be provided as an argument to reverse the
list after sorting. `key` can either be a `DoubleFunction<Double>`, or a `ToIntFunction<String>`. The former will only work for lists of
numbers, whereas the latter will only work for lists of strings. In general, `key` can be any function that can be applied to all items in
the list before comparing. For lists of numbers, `key` must accept a `double` argument and return a `double`, whereas for lists of strings,
`key` must accept a `String` argument and return an `int`. If `key` is not provided, lists of numbers are sorted normally, while lists of
strings are sorted alphabetically. Has no return value.
17) `list.reverse()` reverses `list`. Has no return value.
18) `list.copy()` returns a copy of `list`. `PythonList l = list.copy();` is equivalent to `PythonList l = new PythonList(list);`.
* PythonList methods implementing python functions for lists
19) `list.length()` returns the size of the list. Is equivalent to Python's `len(l)`.
20) `list.max([DoubleFunction<Double> key / ToIntFunction<String> key])` returns the max in `list`. By default, for a list of numbers, the
largest number will be returned, while for a list of strings, lexicographic ordering is used to determine the output. Just like in `sort`,
`key` can be provided to change the way in which items are ordered (therefore changing the maximum). Is equivalent to Python's `max(l)`.
21) `list.min([DoubleFunction<Double> key / ToIntFunction<String> key])` is the opposite of `max`. Is equivalent to Python's `min(l)`.
* Additional methods
22) `list.iterator()` returns an iterator for the list's items.
23) `list.toString()` returns a string representation of the list.

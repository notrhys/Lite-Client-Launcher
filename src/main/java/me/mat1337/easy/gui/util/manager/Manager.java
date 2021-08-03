package me.mat1337.easy.gui.util.manager;

import lombok.Getter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Manager<T> {

    @Getter
    private final List<T> list = new CopyOnWriteArrayList<>();

    public T at(int index) {
        return list.get(index);
    }

    public void push(T item) {
        list.add(item);
    }

    public void push(T item, int index) {
        list.add(index, item);
    }

    public void pop(T item) {
        list.remove(item);
    }

    public void pop(int index) {
        list.remove(index);
    }

    public void popIf(Predicate<? super T> filter) {
        list.removeIf(filter);
    }

    public int indexOf(T item) {
        return list.indexOf(item);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public Stream<T> filter(Predicate<? super T> filter) {
        return stream().filter(filter);
    }

    public Stream<T> sorted() {
        return stream().sorted();
    }

    public Stream<T> sorted(Comparator<? super T> comparator) {
        return stream().sorted(comparator);
    }

    public Stream<T> parallelStream() {
        return getList().parallelStream();
    }

    public Stream<T> parallelFilter(Predicate<? super T> filter) {
        return parallelStream().filter(filter);
    }

    public T find(Predicate<? super T> filter) {
        return filter(filter).findFirst().orElse(null);
    }

    public T parallelFind(Predicate<? super T> filter) {
        return parallelFilter(filter).findFirst().orElse(null);
    }

    public void removeIf(Predicate<? super T> filter) {
        list.removeIf(filter);
    }

    public void sort(Comparator<? super T> comparator) {
        list.sort(comparator);
    }

    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    public T forClass(Class<?> aClass) {
        return find(item -> item.getClass().equals(aClass));
    }

    public boolean contains(T item) {
        return list.contains(item);
    }

    public boolean containsAll(Collection<T> collection) {
        return list.containsAll(collection);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

}
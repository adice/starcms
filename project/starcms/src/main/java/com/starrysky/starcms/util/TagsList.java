package com.starrysky.starcms.util;

/**
 * @ClassName TagsList
 * @Description
 * @Author adi
 * @Date 2022-08-24 14:34
 */
public class TagsList {
    private String[] data;
    private int size = 0;

    public TagsList(int size) {
        data = new String[size];
    }

    public TagsList() {
        this(10);
    }

    public void add(String str) {
        ensureCapacity(size + 1);
        data[size++] = str;
    }

    public String get(int index) {
        if (index < size)
            return data[index];
        else
            return null;
    }

    // 为了提高效率，只将其置为null
    public boolean remove(String str) {
        for (int index = 0; index < size; index++) {
            if (str.equals(data[index])) {
                data[index] = null;
                return true;
            }
        }
        return false;
    }

    public boolean remove(int index) {
        if (index < data.length) {
            data[index] = null;
            return true;
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    // 扩展容量
    public void ensureCapacity(int minSize) {
        int oldCapacity = data.length;
        if (minSize > oldCapacity) {
            int newCapacity = Math.max((oldCapacity * 3 / 2 + 1), minSize);
            String[] newArray = new String[newCapacity];
            for (int i = 0; i < data.length; i++) {
                newArray[i] = data[i];
            }
            data = newArray;
        }
    }
}
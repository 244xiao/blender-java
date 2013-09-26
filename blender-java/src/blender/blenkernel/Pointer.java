package blender.blenkernel;

public interface Pointer<T> {
    public T get();
    public void set(T obj);
}

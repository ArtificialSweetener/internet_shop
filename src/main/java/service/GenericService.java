package service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {
	T create(T element);

    Optional<T> get(Long id);

    List<T> getAll();

    T update(T element);

    boolean delete(Long id);
}

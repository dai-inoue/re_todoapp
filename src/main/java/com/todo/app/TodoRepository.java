package com.todo.app;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todo.app.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}

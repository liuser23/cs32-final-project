import React, { useState } from 'react';
import TodoForm from './TodoForm';
import Todo from './Todo';

function TodoList({code}) {
    const [todos, setTodos] = useState([]);

    const addTodo = todo => {
        if (!todo.text || /^\s*$/.test(todo.text)) {
            return;
        }

        const newTodos = [todo, ...todos];

        setTodos(newTodos);
        console.log(...todos);
    };



    const removeTodo = id => {
        const removedArr = [...todos].filter(todo => todo.id !== id);

        setTodos(removedArr);
    };


    return (
        <>
            <h1>My Recommendations</h1>
            <TodoForm onSubmit={addTodo} code={code} />
            <Todo
                todos={todos}
                removeTodo={removeTodo}
            />
        </>
    );
}

export default TodoList;
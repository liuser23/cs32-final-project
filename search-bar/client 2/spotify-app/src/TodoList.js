// import React, { useState } from 'react';
import React, { useState } from 'react';
import TodoForm from './TodoForm';
import Todo from './Todo';

function TodoList() {
    const [todos, setTodos] = useState([]);

    const addTodo = todo => {
        if (!todo.text || /^\s*$/.test(todo.text)) {
            return;
        }

        const newTodos = [todo, ...todos];

        setTodos(newTodos);
        console.log(...todos);
    };

    const updateTodo = (todoId, newValue) => {
        if (!newValue.text || /^\s*$/.test(newValue.text)) {
            return;
        }

        setTodos(prev => prev.map(item => (item.id === todoId ? newValue : item)));
    };

    const removeTodo = id => {
        const removedArr = [...todos].filter(todo => todo.id !== id);

        setTodos(removedArr);
    };

    const completeTodo = id => {
        let updatedTodos = todos.map(todo => {
            if (todo.id === id) {
                todo.isComplete = !todo.isComplete;
            }
            return todo;
        });
        setTodos(updatedTodos);
    };

    return (
        <>
            <h1>Add Song Recommendation</h1>
            <TodoForm onSubmit={addTodo} />
            <Todo
                todos={todos}
                completeTodo={completeTodo}
                removeTodo={removeTodo}
                updateTodo={updateTodo}
            />
        </>
    );
}

export default TodoList;



// import TodoForm from './TodoForm';
// import Todo from './Todo';
//
// function TodoList({code}) {
//     const [todos, setTodos] = useState([]);
//
//     const addTodo = todo => {
//         if (!todo.text || /^\s*$/.test(todo.text)) {
//             return;
//         }
//
//         const newTodos = [todo, ...todos];
//
//         setTodos(newTodos);
//         console.log(...todos);
//     };
//
//
//
//     const removeTodo = id => {
//         const removedArr = [...todos].filter(todo => todo.id !== id);
//
//         setTodos(removedArr);
//     };
//
//
//     return (
//         <>
//             <h1>My Recommendations</h1>
//             <TodoForm onSubmit={addTodo} code={code} />
//             <Todo
//                 todos={todos}
//                 removeTodo={removeTodo}
//             />
//         </>
//     );
// }
//
// export default TodoList;
import React, { useState } from 'react';

function StudentForm({ addStudent }) {
  const [name, setName] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (name.trim() !== '') {
      addStudent(name);
      setName('');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginTop: '20px' }}>
      <input 
        type="text" 
        placeholder="Имя ученика" 
        value={name} 
        onChange={(e) => setName(e.target.value)} 
        required
      />
      <button type="submit" style={{ marginLeft: '10px' }}>Добавить</button>
    </form>
  );
}

export default StudentForm;

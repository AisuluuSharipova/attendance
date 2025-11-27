import React, { useState } from 'react';

export default function StudentFormModal({ onClose, onSubmit }) {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    studentId: '',
    group: '',
    packageType: '12',
    lessonsUsed: 0,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.name.trim()) {
      onSubmit(formData);
      setFormData({ name: '', email: '', studentId: '', group: '', packageType: '12', lessonsUsed: 0 });
    }
  };

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        background: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
        backdropFilter: 'blur(4px)',
      }}
      onClick={onClose}
    >
      <div
        style={{
          background: 'white',
          borderRadius: 'var(--radius-lg)',
          padding: 'var(--space-32)',
          boxShadow: 'var(--shadow-xl)',
          maxWidth: '400px',
          width: '90%',
          animation: 'slideUp 300ms ease-out',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <h2 style={{ margin: '0 0 var(--space-24) 0', fontSize: '20px', fontWeight: '600' }}>
          Add New Student
        </h2>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: 'var(--space-16)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Full Name *
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="John Doe"
              required
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            />
          </div>

          <div style={{ marginBottom: 'var(--space-16)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Email
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="john@example.com"
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            />
          </div>

          <div style={{ marginBottom: 'var(--space-16)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Student ID
            </label>
            <input
              type="text"
              name="studentId"
              value={formData.studentId}
              onChange={handleChange}
              placeholder="STU006"
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            />
          </div>

          <div style={{ marginBottom: 'var(--space-24)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Group
            </label>
            <input
              type="text"
              name="group"
              value={formData.group}
              onChange={handleChange}
              placeholder="Group A"
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            />
          </div>

          <div style={{ marginBottom: 'var(--space-16)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Package Type
            </label>
            <select
              name="packageType"
              value={formData.packageType}
              onChange={handleChange}
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            >
              <option value="12">12 Lessons</option>
              <option value="24">24 Lessons</option>
            </select>
          </div>

          <div style={{ marginBottom: 'var(--space-24)' }}>
            <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-700)', display: 'block', marginBottom: '6px' }}>
              Initial Lessons Used
            </label>
            <input
              type="number"
              name="lessonsUsed"
              min="0"
              value={formData.lessonsUsed}
              onChange={(e) => setFormData({ ...formData, lessonsUsed: Number(e.target.value) })}
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--gray-300)',
                borderRadius: 'var(--radius-md)',
                fontSize: '14px',
                fontFamily: 'var(--font-family)',
              }}
            />
          </div>

          <div style={{ display: 'flex', gap: 'var(--space-12)', justifyContent: 'flex-end' }}>
            <button
              type="button"
              onClick={onClose}
              style={{
                padding: '10px 20px',
                borderRadius: 'var(--radius-md)',
                border: '1px solid var(--gray-300)',
                background: 'white',
                color: 'var(--gray-700)',
                fontWeight: '500',
                cursor: 'pointer',
                fontSize: '14px',
                transition: 'all var(--transition-base)',
              }}
              onMouseEnter={(e) => {
                e.target.style.background = 'var(--gray-100)';
              }}
              onMouseLeave={(e) => {
                e.target.style.background = 'white';
              }}
            >
              Cancel
            </button>
            <button
              type="submit"
              style={{
                padding: '10px 20px',
                borderRadius: 'var(--radius-md)',
                border: 'none',
                background: 'var(--primary-600)',
                color: 'white',
                fontWeight: '500',
                cursor: 'pointer',
                fontSize: '14px',
                transition: 'all var(--transition-base)',
              }}
              onMouseEnter={(e) => {
                e.target.style.background = 'var(--primary-700)';
              }}
              onMouseLeave={(e) => {
                e.target.style.background = 'var(--primary-600)';
              }}
            >
              Add Student
            </button>
          </div>
        </form>

        <style>{`
          @keyframes slideUp {
            from {
              opacity: 0;
              transform: translateY(20px);
            }
            to {
              opacity: 1;
              transform: translateY(0);
            }
          }
        `}</style>
      </div>
    </div>
  );
}

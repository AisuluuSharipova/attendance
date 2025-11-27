import React, { useState } from 'react';

export default function AttendanceHeader({ onSearch, onFilterChange, dateFilter, setDateFilter }) {
  const [searchQuery, setSearchQuery] = useState('');
  const [showFilters, setShowFilters] = useState(false);
  const [filterType, setFilterType] = useState('all');

  const handleSearch = (e) => {
    const value = e.target.value;
    setSearchQuery(value);
    onSearch(value);
  };

  const handleFilterChange = (type) => {
    setFilterType(type);
    onFilterChange(type);
  };

  return (
    <div className="attendance-header">
      <div className="header-left">
        <h1 className="header-title">Attendance List</h1>
        <div className="search-bar">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Search by name..."
            value={searchQuery}
            onChange={handleSearch}
          />
        </div>
      </div>

      <div className="header-actions">
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          <label style={{ fontSize: '13px', fontWeight: '500', color: 'var(--gray-600)' }}>
            Date:
          </label>
          <input
            type="date"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
            style={{ 
              padding: '6px 10px', 
              borderRadius: 'var(--radius-md)', 
              border: '1px solid var(--gray-300)',
              fontSize: '13px',
              cursor: 'pointer'
            }}
          />
        </div>

        <button
          className="header-button"
          onClick={() => setShowFilters(!showFilters)}
          title="Filter options"
        >
          🎛️ Filter
        </button>
      </div>

      <div className="profile-icon">AC</div>

      {showFilters && (
        <div
          style={{
            position: 'absolute',
            top: '80px',
            right: '24px',
            background: 'white',
            border: '1px solid var(--gray-200)',
            borderRadius: 'var(--radius-md)',
            boxShadow: 'var(--shadow-lg)',
            zIndex: 1000,
            minWidth: '180px',
            animation: 'slideDown 200ms ease-out',
          }}
        >
          <div style={{ padding: 'var(--space-12) 0' }}>
            {['all', 'present', 'absent', 'late'].map((status) => (
              <button
                key={status}
                onClick={() => {
                  handleFilterChange(status);
                  setShowFilters(false);
                }}
                style={{
                  display: 'block',
                  width: '100%',
                  padding: 'var(--space-10) var(--space-16)',
                  background: filterType === status ? 'var(--primary-50)' : 'transparent',
                  border: 'none',
                  textAlign: 'left',
                  cursor: 'pointer',
                  fontSize: '13px',
                  color: filterType === status ? 'var(--primary-600)' : 'var(--gray-700)',
                  fontWeight: filterType === status ? '600' : '400',
                  transition: 'all var(--transition-base)',
                }}
                onMouseEnter={(e) => {
                  e.target.style.background = 'var(--gray-100)';
                }}
                onMouseLeave={(e) => {
                  e.target.style.background =
                    filterType === status ? 'var(--primary-50)' : 'transparent';
                }}
              >
                {status === 'all' ? '📋 All Students' : `${status.charAt(0).toUpperCase() + status.slice(1)}`}
              </button>
            ))}
          </div>
        </div>
      )}

      <style>{`
        @keyframes slideDown {
          from {
            opacity: 0;
            transform: translateY(-8px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>
    </div>
  );
}

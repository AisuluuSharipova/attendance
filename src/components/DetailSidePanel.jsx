import React from 'react';

export default function DetailSidePanel({ selectedStudent, attendanceHistory, onClose }) {
  if (!selectedStudent) {
    return null;
  }

  const recentHistory = (attendanceHistory[selectedStudent.id] || []).slice(0, 5);

  const getStatusColor = (status) => {
    switch (status) {
      case 'present':
        return 'present';
      case 'absent':
        return 'absent';
      case 'late':
        return 'late';
      default:
        return 'absent';
    }
  };

  const getInitials = (name) => {
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };

  return (
    <div className="side-panel">
      {/* Close Button */}
      {onClose && (
        <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 'var(--space-12)' }}>
          <button
            onClick={onClose}
            style={{
              background: 'none',
              border: 'none',
              fontSize: '20px',
              cursor: 'pointer',
              padding: '4px 8px',
              color: 'var(--gray-500)',
              transition: 'color var(--transition-base)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
            onMouseEnter={(e) => e.target.style.color = 'var(--gray-900)'}
            onMouseLeave={(e) => e.target.style.color = 'var(--gray-500)'}
            title="Close panel"
            aria-label="Close detail panel"
          >
            ✕
          </button>
        </div>
      )}

      {/* Student Header */}
      <div style={{ marginBottom: 'var(--space-24)', paddingBottom: 'var(--space-20)', borderBottom: '1px solid var(--gray-100)' }}>
        <div
          style={{
            width: '60px',
            height: '60px',
            borderRadius: 'var(--radius-lg)',
            background: 'linear-gradient(135deg, var(--primary-500), var(--primary-600))',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            fontWeight: '600',
            fontSize: '20px',
            marginBottom: 'var(--space-12)',
          }}
        >
          {getInitials(selectedStudent.name)}
        </div>
        <h3 style={{ margin: '0 0 4px 0', fontSize: '16px', fontWeight: '600', color: 'var(--gray-900)' }}>
          {selectedStudent.name}
        </h3>
        <p style={{ margin: '0', fontSize: '12px', color: 'var(--gray-600)' }}>
          {selectedStudent.email || 'No email'}
        </p>
      </div>

      {/* Quick Stats */}
      <div className="panel-section">
        <p className="panel-label">📊 Current Status</p>
        <div
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: 'var(--space-8)',
            padding: '8px 12px',
            background: `var(--${getStatusColor(selectedStudent.status)}-50)`,
            borderRadius: 'var(--radius-full)',
            fontSize: '12px',
            fontWeight: '600',
            color: `var(--${getStatusColor(selectedStudent.status) === 'present' ? 'success' : getStatusColor(selectedStudent.status) === 'late' ? 'warning' : 'destructive'}-600)`,
          }}
        >
          <span className="status-dot"></span>
          {selectedStudent.status.toUpperCase()}
        </div>

        {selectedStudent.checkInTime && (
          <div style={{ marginTop: 'var(--space-12)' }}>
            <p className="panel-label" style={{ marginBottom: '4px' }}>⏰ Check-in Time</p>
            <p className="panel-value">
              {new Date(selectedStudent.checkInTime).toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: true,
              })}
            </p>
          </div>
        )}
      </div>

      {/* Student Info */}
      <div className="panel-section">
        <p className="panel-label">ℹ️ Details</p>
        <div style={{ marginBottom: 'var(--space-12)' }}>
          <p style={{ margin: '0 0 2px 0', fontSize: '11px', color: 'var(--gray-500)', textTransform: 'uppercase', letterSpacing: '0.5px', fontWeight: '600' }}>
            Student ID
          </p>
          <p className="panel-value">{selectedStudent.studentId || '—'}</p>
        </div>
        <div>
          <p style={{ margin: '0 0 2px 0', fontSize: '11px', color: 'var(--gray-500)', textTransform: 'uppercase', letterSpacing: '0.5px', fontWeight: '600' }}>
            Group
          </p>
          <p className="panel-value">{selectedStudent.group || '—'}</p>
        </div>
      </div>

      {/* Package Info */}
      <div className="panel-section">
        <p className="panel-label">📦 Package</p>
        <div style={{ marginBottom: 'var(--space-12)' }}>
          <p style={{ margin: '0 0 2px 0', fontSize: '11px', color: 'var(--gray-500)', textTransform: 'uppercase', letterSpacing: '0.5px', fontWeight: '600' }}>
            Package Type
          </p>
          <p className="panel-value">{selectedStudent.packageType ? `${selectedStudent.packageType} lessons` : '—'}</p>
        </div>
        <div style={{ marginBottom: 'var(--space-12)' }}>
          <p style={{ margin: '0 0 2px 0', fontSize: '11px', color: 'var(--gray-500)', textTransform: 'uppercase', letterSpacing: '0.5px', fontWeight: '600' }}>
            Used
          </p>
          <p className="panel-value">{typeof selectedStudent.lessonsUsed === 'number' ? selectedStudent.lessonsUsed : (selectedStudent.lessonsUsed || 0)}</p>
        </div>
        <div>
          <p style={{ margin: '0 0 2px 0', fontSize: '11px', color: 'var(--gray-500)', textTransform: 'uppercase', letterSpacing: '0.5px', fontWeight: '600' }}>
            Remaining
          </p>
          {(() => {
            let remaining = null;
            if (typeof selectedStudent.packageType === 'number') {
              remaining = selectedStudent.packageType - (typeof selectedStudent.lessonsUsed === 'number' ? selectedStudent.lessonsUsed : (selectedStudent.lessonsUsed ? parseInt(selectedStudent.lessonsUsed, 10) : 0));
            } else if (typeof selectedStudent.lessonsRemaining === 'number') {
              remaining = selectedStudent.lessonsRemaining;
            }
            const isLow = remaining !== null && remaining <= 2;
            return (
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <div style={{ fontWeight: 700, color: isLow ? 'var(--destructive-600)' : 'var(--success-600)' }}>{remaining !== null ? remaining : '—'}</div>
                {isLow && (
                  <div className="warning-alert critical" style={{ padding: '6px 10px', margin: 0 }}>
                    <div className="warning-icon" aria-hidden>⚠️</div>
                    <div style={{ fontSize: 12, fontWeight: 700, color: 'var(--destructive-600)' }}>Low</div>
                  </div>
                )}
              </div>
            );
          })()}
        </div>
      </div>

      {/* Attendance History */}
      {recentHistory.length > 0 && (
        <div className="panel-section">
          <p className="panel-label">📋 Last 5 Days</p>
          <div>
            {recentHistory.map((entry, idx) => (
              <div key={idx} className="history-item">
                <span className="history-date">
                  {new Date(entry.date).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                  })}
                </span>
                <span className={`history-status ${entry.status}`}>
                  {entry.status === 'present' && '✓'}
                  {entry.status === 'absent' && '✕'}
                  {entry.status === 'late' && '⏱'}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Notes Section */}
      <div className="panel-section">
        <p className="panel-label">📝 Notes</p>
        <textarea
          placeholder="Add notes..."
          style={{
            width: '100%',
            minHeight: '80px',
            padding: 'var(--space-8)',
            border: '1px solid var(--gray-200)',
            borderRadius: 'var(--radius-md)',
            fontSize: '12px',
            fontFamily: 'var(--font-family)',
            color: 'var(--gray-900)',
            resize: 'vertical',
            fontWeight: '400',
          }}
          defaultValue={selectedStudent.notes || ''}
        />
      </div>
    </div>
  );
}

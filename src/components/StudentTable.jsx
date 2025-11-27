import React, { useState } from 'react';

export default function StudentTable({
  students,
  markAttendance,
  onSelectStudent,
  filterStatus,
  searchQuery,
}) {
  const [sortColumn, setSortColumn] = useState('name');
  const [sortOrder, setSortOrder] = useState('asc');

  const getInitials = (name) => {
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };

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

  const getCheckInTime = (student) => {
    if (!student.checkInTime) return '—';
    const time = new Date(student.checkInTime);
    return time.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true,
    });
  };

  const filteredStudents = students.filter((student) => {
    const matchesFilter =
      filterStatus === 'all' || student.status === filterStatus;
    const matchesSearch =
      !searchQuery ||
      student.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (student.studentId &&
        student.studentId.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesFilter && matchesSearch;
  });

  const handleExport = () => {
    const csv = [
      ['Name', 'ID', 'Group', 'Status', 'Check-in Time', 'Remaining Lessons'],
      ...filteredStudents.map((s) => [
        s.name,
        s.studentId || '—',
        s.group || '—',
        s.status,
        getCheckInTime(s),
        (s.packageType ? s.packageType - (s.lessonsUsed || 0) : '—'),
      ]),
    ]
      .map((row) => row.map((cell) => `"${cell}"`).join(','))
      .join('\n');

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `attendance_${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
  };

  const handleMarkAllPresent = () => {
    filteredStudents.forEach((student, idx) => {
      const originalIdx = students.indexOf(student);
      if (originalIdx !== -1 && students[originalIdx].status !== 'present') {
        markAttendance(originalIdx, 'present');
      }
    });
  };

  if (students.length === 0) {
    return (
      <div className="table-container">
        <div className="empty-state">
          <div className="empty-icon">📭</div>
          <h3 className="empty-title">No Students Yet</h3>
          <p className="empty-description">
            Add students to start tracking attendance
          </p>
        </div>
      </div>
    );
  }

  if (filteredStudents.length === 0) {
    return (
      <div className="table-container">
        <div className="empty-state">
          <div className="empty-icon">🔍</div>
          <h3 className="empty-title">No Results Found</h3>
          <p className="empty-description">
            Try adjusting your search or filters
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="table-container">
      <div className="table-header">
        <h3 className="table-header-title">
          📊 {filteredStudents.length} of {students.length} Students
        </h3>
        <div className="table-actions">
          <button
            className="action-button primary"
            onClick={handleMarkAllPresent}
            title="Mark all filtered students as present"
          >
            ✓ Mark All Present
          </button>
          <button
            className="action-button"
            onClick={handleExport}
            title="Export as CSV"
          >
            ⬇️ Export
          </button>
        </div>
      </div>

      <div style={{ overflowX: 'auto' }}>
        <table>
          <thead>
            <tr>
              <th style={{ width: '30%' }}>Student</th>
              <th style={{ width: '10%' }}>ID</th>
              <th style={{ width: '12%' }}>Group</th>
              <th style={{ width: '12%' }}>Status</th>
              <th style={{ width: '12%' }}>Check-in</th>
              <th style={{ width: '12%' }}>Remaining</th>
              <th style={{ width: '12%' }}>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredStudents.map((student, idx) => {
              const originalIdx = students.indexOf(student);
              // Robust remaining calculation: prefer explicit packageType/lessonsUsed,
              // fallback to possible alternative fields if present.
              let remaining = null;
              if (typeof student.packageType === 'number') {
                remaining = student.packageType - (typeof student.lessonsUsed === 'number' ? student.lessonsUsed : (student.lessonsUsed ? parseInt(student.lessonsUsed, 10) : 0));
              } else if (typeof student.lessonsRemaining === 'number') {
                remaining = student.lessonsRemaining;
              } else if (typeof student.remainingLessons === 'number') {
                remaining = student.remainingLessons;
              } else if (student.package && typeof student.package.total === 'number') {
                const used = typeof student.package.used === 'number' ? student.package.used : (student.package.used ? parseInt(student.package.used, 10) : 0);
                remaining = student.package.total - used;
              }
              const isLow = remaining !== null && remaining <= 2;
              return (
                <tr key={idx} onClick={() => onSelectStudent && onSelectStudent(student)}>
                  <td>
                    <div className="student-info">
                      <div className="student-avatar">
                        {getInitials(student.name)}
                      </div>
                      <div className="student-details">
                        <h4>{student.name}</h4>
                        <p>{student.email || 'No email'}</p>
                      </div>
                    </div>
                  </td>
                  <td>{student.studentId || '—'}</td>
                  <td>{student.group || '—'}</td>
                  <td>
                    <span className={`status-badge ${getStatusColor(student.status)}`}>
                      <span className="status-dot"></span>
                      {student.status.charAt(0).toUpperCase() + student.status.slice(1)}
                    </span>
                  </td>
                  <td>
                    <span className="time-display">{getCheckInTime(student)}</span>
                  </td>
                  <td>
                    {remaining === null ? '—' : (
                      <div className={`remaining-cell ${isLow ? 'remaining-low' : 'remaining-safe'}`}>
                        {isLow ? (
                          <>
                            <svg className="warning-icon" xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden title={`Only ${remaining} lesson${remaining !== 1 ? 's' : ''} remaining`}>
                              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                              <line x1="12" y1="9" x2="12" y2="13"></line>
                              <line x1="12" y1="17" x2="12.01" y2="17"></line>
                            </svg>
                            <span className="remaining-number">{remaining}</span>
                          </>
                        ) : (
                          <span className="remaining-number">{remaining}</span>
                        )}
                      </div>
                    )}
                  </td>
                  <td>
                    <select
                      className="status-dropdown"
                      value={student.status}
                      onChange={(e) => {
                        e.stopPropagation();
                        markAttendance(originalIdx, e.target.value);
                      }}
                    >
                      <option value="present">Present</option>
                      <option value="absent">Absent</option>
                      <option value="late">Late</option>
                    </select>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

import React, { useState, useEffect } from 'react';
import AttendanceHeader from './AttendanceHeader';
import StatisticsCards from './StatisticsCards';
import StudentTable from './StudentTable';
import DetailSidePanel from './DetailSidePanel';
import StudentFormModal from './StudentFormModal';

export default function AttendanceDashboard() {
  const [students, setStudents] = useState([
    {
      id: 1,
      name: 'Alice Johnson',
      email: 'alice@university.edu',
      studentId: 'STU001',
      group: 'Group A',
      status: 'present',
      checkInTime: new Date().toISOString(),
      packageType: 12,
      lessonsUsed: 10,
      notes: '',
    },
    {
      id: 2,
      name: 'Bob Smith',
      email: 'bob@university.edu',
      studentId: 'STU002',
      group: 'Group B',
      status: 'present',
      checkInTime: new Date(Date.now() - 5 * 60000).toISOString(),
      packageType: 24,
      lessonsUsed: 5,
      notes: '',
    },
    {
      id: 3,
      name: 'Carol White',
      email: 'carol@university.edu',
      studentId: 'STU003',
      group: 'Group A',
      status: 'late',
      checkInTime: new Date(Date.now() - 15 * 60000).toISOString(),
      packageType: 12,
      lessonsUsed: 8,
      notes: '',
    },
    {
      id: 4,
      name: 'David Brown',
      email: 'david@university.edu',
      studentId: 'STU004',
      group: 'Group C',
      status: 'absent',
      checkInTime: null,
      packageType: 12,
      lessonsUsed: 11,
      notes: '',
    },
    {
      id: 5,
      name: 'Eva Davis',
      email: 'eva@university.edu',
      studentId: 'STU005',
      group: 'Group B',
      status: 'present',
      checkInTime: new Date(Date.now() - 10 * 60000).toISOString(),
      packageType: 24,
      lessonsUsed: 2,
      notes: '',
    },
  ]);

  const [searchQuery, setSearchQuery] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [dateFilter, setDateFilter] = useState(new Date().toISOString().split('T')[0]);
  const [showAddStudent, setShowAddStudent] = useState(false);
  const [attendanceHistory, setAttendanceHistory] = useState({});

  // Calculate statistics
  const stats = {
    total: students.length,
    present: students.filter((s) => s.status === 'present').length,
    absent: students.filter((s) => s.status === 'absent').length,
    late: students.filter((s) => s.status === 'late').length,
  };

  const markAttendance = (index, status) => {
    const newStudents = [...students];
    newStudents[index].status = status;

    // Record check-in time
    if (status !== 'absent') {
      newStudents[index].checkInTime = new Date().toISOString();
    } else {
      newStudents[index].checkInTime = null;
    }

    setStudents(newStudents);

    // Add to history
    const studentId = newStudents[index].id;
    const history = attendanceHistory[studentId] || [];
    history.unshift({
      date: dateFilter,
      status: status,
    });
    setAttendanceHistory({
      ...attendanceHistory,
      [studentId]: history.slice(0, 30), // Keep last 30 records
    });

    // Update selected student if it's the one being marked
    if (selectedStudent && selectedStudent.id === studentId) {
      setSelectedStudent(newStudents[index]);
    }
  };

  const addStudent = (studentData) => {
    const newStudent = {
      id: Math.max(...students.map((s) => s.id), 0) + 1,
      ...studentData,
      packageType: studentData.packageType ? parseInt(studentData.packageType, 10) : undefined,
      lessonsUsed: typeof studentData.lessonsUsed === 'number' ? studentData.lessonsUsed : (studentData.lessonsUsed ? parseInt(studentData.lessonsUsed, 10) : 0),
      status: 'absent',
      checkInTime: null,
      notes: '',
    };
    setStudents([...students, newStudent]);
    setShowAddStudent(false);
  };

  const deleteStudent = (index) => {
    const newStudents = students.filter((_, i) => i !== index);
    setStudents(newStudents);
    if (selectedStudent && selectedStudent.id === students[index].id) {
      setSelectedStudent(null);
    }
  };

  useEffect(() => {
    // Update selected student when students change
    if (selectedStudent) {
      const updated = students.find((s) => s.id === selectedStudent.id);
      if (updated) {
        setSelectedStudent(updated);
      }
    }
  }, [students]);

  return (
    <div className="dashboard">
      {/* Header */}
      <AttendanceHeader
        onSearch={setSearchQuery}
        onFilterChange={setFilterStatus}
        dateFilter={dateFilter}
        setDateFilter={setDateFilter}
      />

      {/* Statistics */}
      <StatisticsCards stats={stats} />

      {/* Main Content */}
      <div className="content-wrapper">
        {/* Table */}
        <StudentTable
          students={students}
          markAttendance={markAttendance}
          onSelectStudent={setSelectedStudent}
          filterStatus={filterStatus}
          searchQuery={searchQuery}
        />

        {/* Side Panel */}
        <DetailSidePanel 
          selectedStudent={selectedStudent} 
          attendanceHistory={attendanceHistory}
          onClose={() => setSelectedStudent(null)}
        />
      </div>

      {/* Mobile Card View */}
      <div style={{ display: 'none' }} className="mobile-cards">
        {students.map((student, idx) => (
          <div key={idx} className="student-card" onClick={() => setSelectedStudent(student)}>
            <div className="card-header">
              <div
                className="student-avatar"
                style={{
                  width: '36px',
                  height: '36px',
                  fontSize: '14px',
                }}
              >
                {student.name
                  .split(' ')
                  .map((n) => n[0])
                  .join('')
                  .toUpperCase()
                  .slice(0, 2)}
              </div>
              <div className="card-info">
                <p className="card-name">{student.name}</p>
                <p className="card-meta">{student.studentId || 'No ID'}</p>
              </div>
            </div>
            <div className="card-footer">
              <span className={`status-badge ${student.status}`} style={{ fontSize: '11px' }}>
                {student.status.toUpperCase()}
              </span>
              <div className="card-actions">
                <button
                  className="card-action-btn"
                  onClick={(e) => {
                    e.stopPropagation();
                    markAttendance(idx, 'present');
                  }}
                >
                  ✓
                </button>
                <button
                  className="card-action-btn"
                  onClick={(e) => {
                    e.stopPropagation();
                    markAttendance(idx, 'absent');
                  }}
                >
                  ✕
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Floating Action Button */}
      <button
        className="floating-action-btn"
        onClick={() => setShowAddStudent(true)}
        title="Add new student"
      >
        +
      </button>

      {/* Add Student Modal */}
      {showAddStudent && (
        <StudentFormModal onClose={() => setShowAddStudent(false)} onSubmit={addStudent} />
      )}
    </div>
  );
}

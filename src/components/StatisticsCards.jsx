import React from 'react';

export default function StatisticsCards({ stats }) {
  const statConfigs = [
    {
      key: 'total',
      label: 'Total Students',
      value: stats.total || 0,
      className: 'total',
      icon: '👥',
    },
    {
      key: 'present',
      label: 'Present',
      value: stats.present || 0,
      className: 'present',
      icon: '✓',
    },
    {
      key: 'absent',
      label: 'Absent',
      value: stats.absent || 0,
      className: 'absent',
      icon: '✕',
    },
    {
      key: 'late',
      label: 'Late',
      value: stats.late || 0,
      className: 'late',
      icon: '⏱️',
    },
  ];

  return (
    <div className="stats-container">
      {statConfigs.map((stat) => (
        <div key={stat.key} className={`stat-card ${stat.className}`}>
          <p className="stat-label">{stat.label}</p>
          <h2 className="stat-value">{stat.value}</h2>
        </div>
      ))}
    </div>
  );
}

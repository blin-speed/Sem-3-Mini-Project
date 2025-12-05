import React, { useState, useEffect, useRef } from 'react';
import html2canvas from 'html2canvas';
import './TaskPlanner.css';

const TaskPlanner = () => {
  const [tasks, setTasks] = useState([]);
  const [input, setInput] = useState('');
  const [insertAfter, setInsertAfter] = useState(-1);
  const [selectedIndex, setSelectedIndex] = useState(null);
  const flowchartRef = useRef(null);

  useEffect(() => {
    const saved = JSON.parse(localStorage.getItem('tasks')) || [];
    setTasks(saved);
  }, []);

  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }, [tasks]);

  const handleAddTask = () => {
    if (!input.trim()) return;
    const newTasks = [...tasks];
    const index = insertAfter === -1 ? tasks.length : insertAfter + 1;
    newTasks.splice(index, 0, input.trim());
    setTasks(newTasks);
    setInput('');
    setInsertAfter(-1);
  };

  const handleDeleteTask = () => {
    if (selectedIndex === null) return;
    const newTasks = [...tasks];
    newTasks.splice(selectedIndex, 1);
    setTasks(newTasks);
    setSelectedIndex(null);
  };

  const handleClear = () => {
    setTasks([]);
    localStorage.removeItem('tasks');
    setSelectedIndex(null);
  };

  const handleDownload = () => {
    if (flowchartRef.current) {
      html2canvas(flowchartRef.current).then((canvas) => {
        const link = document.createElement('a');
        link.download = 'tasks.jpg';
        link.href = canvas.toDataURL('image/jpeg');
        link.click();
      });
    }
  };

  return (
    <div className="planner-wrapper">
      {/* Fixed header at the top */}
      <header className="planner-header">
        <h2>Daily Task Planner</h2>
      </header>

      {/* Centered content */}
      <div className="planner-content">
        <div className="flowchart" ref={flowchartRef}>
          {tasks.map((task, i) => (
            <React.Fragment key={i}>
              {i > 0 && <div className="arrow">→</div>}
              <div
                className={`box ${selectedIndex === i ? 'selected' : ''}`}
                onClick={() => setSelectedIndex(i)}
              >
                {task}
              </div>
            </React.Fragment>
          ))}
        </div>
      </div>

      {/* Floating bottom controls */}
      <div className="planner-controls">
        <div className="input-row">
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Enter a task"
            onKeyPress={(e) => e.key === 'Enter' && handleAddTask()}
          />
          <select
            value={insertAfter}
            onChange={(e) => setInsertAfter(parseInt(e.target.value))}
          >
            <option value={-1}>Add at end</option>
            {tasks.map((task, i) => (
              <option key={i} value={i}>
                After "{task}"
              </option>
            ))}
          </select>
          <button onClick={handleAddTask}>Add Task</button>
        </div>

        <div className="action-row">
          <button onClick={handleDeleteTask} disabled={selectedIndex === null}>
            Delete Selected
          </button>
          <button onClick={handleClear}>Clear All</button>
          <button onClick={handleDownload}>Download JPG</button>
        </div>
      </div>
    </div>
  );
};

export default TaskPlanner;
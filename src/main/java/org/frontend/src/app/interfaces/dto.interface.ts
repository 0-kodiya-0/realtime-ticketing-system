export interface ThreadCounts {
  usedCount: number;
  runningCount: number;
  interruptedCount: number;
}

export interface PoolStats {
  currentSize: number;
  addedTickets: number;
  removedTickets: number;
  lastUpdateTime: Date;
  timeSinceLastUpdate: string;
}

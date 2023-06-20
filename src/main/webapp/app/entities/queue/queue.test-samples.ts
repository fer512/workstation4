import { QueueStatus } from 'app/entities/enumerations/queue-status.model';

import { IQueue, NewQueue } from './queue.model';

export const sampleWithRequiredData: IQueue = {
  id: 19441,
  name: 'consequently tan Licensed',
  status: 'ACTIVE',
};

export const sampleWithPartialData: IQueue = {
  id: 7049,
  name: 'Island streamline productive',
  status: 'ACTIVE',
};

export const sampleWithFullData: IQueue = {
  id: 38608,
  name: 'Wooden ampere Customer',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewQueue = {
  name: 'matrix Account',
  status: 'DISABLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

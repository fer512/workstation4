import { WorkerStatus } from 'app/entities/enumerations/worker-status.model';

import { IWorker, NewWorker } from './worker.model';

export const sampleWithRequiredData: IWorker = {
  id: 22259,
  name: 'payment Bacon masculinise',
  status: 'ACTIVE',
};

export const sampleWithPartialData: IWorker = {
  id: 87766,
  name: 'Division copying Buckinghamshire',
  status: 'ACTIVE',
};

export const sampleWithFullData: IWorker = {
  id: 33988,
  name: 'Jeep',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWorker = {
  name: 'quod',
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

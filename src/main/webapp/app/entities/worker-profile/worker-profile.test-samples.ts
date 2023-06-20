import { WorkerProfileStatus } from 'app/entities/enumerations/worker-profile-status.model';

import { IWorkerProfile, NewWorkerProfile } from './worker-profile.model';

export const sampleWithRequiredData: IWorkerProfile = {
  id: 74807,
  name: 'black',
  status: 'ACTIVE',
};

export const sampleWithPartialData: IWorkerProfile = {
  id: 34550,
  name: 'Mini connecting quo',
  status: 'ACTIVE',
};

export const sampleWithFullData: IWorkerProfile = {
  id: 95228,
  name: 'yellow Coupe capacitor',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWorkerProfile = {
  name: 'CLI ipsam transmitter',
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { WorkerProfileAttencionChannelType } from 'app/entities/enumerations/worker-profile-attencion-channel-type.model';

import { IWorkerProfileAttencionChannel, NewWorkerProfileAttencionChannel } from './worker-profile-attencion-channel.model';

export const sampleWithRequiredData: IWorkerProfileAttencionChannel = {
  id: 55762,
  name: 'Plastic',
  type: 'VIRTUAL',
};

export const sampleWithPartialData: IWorkerProfileAttencionChannel = {
  id: 63425,
  name: 'aha payment Classical',
  type: 'VIRTUAL',
};

export const sampleWithFullData: IWorkerProfileAttencionChannel = {
  id: 90834,
  name: 'North metrics',
  type: 'VIRTUAL',
};

export const sampleWithNewData: NewWorkerProfileAttencionChannel = {
  name: 'Account',
  type: 'VIRTUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

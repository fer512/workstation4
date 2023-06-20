import { AttencionChannelType } from 'app/entities/enumerations/attencion-channel-type.model';

import { IAttencionChannel, NewAttencionChannel } from './attencion-channel.model';

export const sampleWithRequiredData: IAttencionChannel = {
  id: 77463,
  name: 'Planner',
  type: 'VIRTUAL',
};

export const sampleWithPartialData: IAttencionChannel = {
  id: 32036,
  name: 'West oof Electronic',
  type: 'PRESENTIAL',
};

export const sampleWithFullData: IAttencionChannel = {
  id: 21833,
  name: 'Computer rowdy',
  type: 'VIRTUAL',
};

export const sampleWithNewData: NewAttencionChannel = {
  name: 'alarm cultivate Garden',
  type: 'MIXED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

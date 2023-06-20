import { WaitingRoomAttencionChannelType } from 'app/entities/enumerations/waiting-room-attencion-channel-type.model';

import { IWaitingRoomAttencionChannel, NewWaitingRoomAttencionChannel } from './waiting-room-attencion-channel.model';

export const sampleWithRequiredData: IWaitingRoomAttencionChannel = {
  id: 22428,
  name: 'Tasty Fish',
  type: 'PRESENTIAL',
};

export const sampleWithPartialData: IWaitingRoomAttencionChannel = {
  id: 1309,
  name: 'lavender',
  type: 'VIRTUAL',
};

export const sampleWithFullData: IWaitingRoomAttencionChannel = {
  id: 67208,
  name: 'Credit',
  type: 'MIXED',
};

export const sampleWithNewData: NewWaitingRoomAttencionChannel = {
  name: 'Pants loftily',
  type: 'MIXED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

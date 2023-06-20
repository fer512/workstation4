import { WaitingRoomStatus } from 'app/entities/enumerations/waiting-room-status.model';

import { IWaitingRoom, NewWaitingRoom } from './waiting-room.model';

export const sampleWithRequiredData: IWaitingRoom = {
  id: 74111,
  name: 'Investor',
  status: 'DISABLED',
};

export const sampleWithPartialData: IWaitingRoom = {
  id: 12306,
  name: 'Won',
  status: 'ACTIVE',
};

export const sampleWithFullData: IWaitingRoom = {
  id: 96046,
  name: '1080p Market',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWaitingRoom = {
  name: 'Northwest',
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

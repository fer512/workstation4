import { IOrderQueue, NewOrderQueue } from './order-queue.model';

export const sampleWithRequiredData: IOrderQueue = {
  id: 73442,
  order: 81267,
};

export const sampleWithPartialData: IOrderQueue = {
  id: 10417,
  order: 37757,
};

export const sampleWithFullData: IOrderQueue = {
  id: 58444,
  order: 49444,
};

export const sampleWithNewData: NewOrderQueue = {
  order: 56781,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

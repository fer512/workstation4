import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'worker',
        data: { pageTitle: 'jhipsterSampleApplicationApp.worker.home.title' },
        loadChildren: () => import('./worker/worker.routes'),
      },
      {
        path: 'attencion-channel',
        data: { pageTitle: 'jhipsterSampleApplicationApp.attencionChannel.home.title' },
        loadChildren: () => import('./attencion-channel/attencion-channel.routes'),
      },
      {
        path: 'waiting-room-attencion-channel',
        data: { pageTitle: 'jhipsterSampleApplicationApp.waitingRoomAttencionChannel.home.title' },
        loadChildren: () => import('./waiting-room-attencion-channel/waiting-room-attencion-channel.routes'),
      },
      {
        path: 'worker-profile',
        data: { pageTitle: 'jhipsterSampleApplicationApp.workerProfile.home.title' },
        loadChildren: () => import('./worker-profile/worker-profile.routes'),
      },
      {
        path: 'worker-profile-attencion-channel',
        data: { pageTitle: 'jhipsterSampleApplicationApp.workerProfileAttencionChannel.home.title' },
        loadChildren: () => import('./worker-profile-attencion-channel/worker-profile-attencion-channel.routes'),
      },
      {
        path: 'waiting-room',
        data: { pageTitle: 'jhipsterSampleApplicationApp.waitingRoom.home.title' },
        loadChildren: () => import('./waiting-room/waiting-room.routes'),
      },
      {
        path: 'branch',
        data: { pageTitle: 'jhipsterSampleApplicationApp.branch.home.title' },
        loadChildren: () => import('./branch/branch.routes'),
      },
      {
        path: 'queue',
        data: { pageTitle: 'jhipsterSampleApplicationApp.queue.home.title' },
        loadChildren: () => import('./queue/queue.routes'),
      },
      {
        path: 'company',
        data: { pageTitle: 'jhipsterSampleApplicationApp.company.home.title' },
        loadChildren: () => import('./company/company.routes'),
      },
      {
        path: 'order-queue',
        data: { pageTitle: 'jhipsterSampleApplicationApp.orderQueue.home.title' },
        loadChildren: () => import('./order-queue/order-queue.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

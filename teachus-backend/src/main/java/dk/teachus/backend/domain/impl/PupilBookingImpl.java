/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.domain.impl;

import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;

public class PupilBookingImpl extends BookingImpl implements PupilBooking {
	private static final long serialVersionUID = 1L;

	private Pupil pupil;

	private boolean notificationSent;
	
	private boolean pupilNotificationSent;
	
	private boolean paid;

	public Pupil getPupil() {
		return pupil;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public void setPupil(Pupil pupil) {
		this.pupil = pupil;
	}

	public boolean isPupilNotificationSent() {
		return pupilNotificationSent;
	}

	public void setPupilNotificationSent(boolean pupilNotificationSent) {
		this.pupilNotificationSent = pupilNotificationSent;
	}

}

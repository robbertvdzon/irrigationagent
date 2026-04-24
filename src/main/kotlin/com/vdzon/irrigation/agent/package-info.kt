/**
 * Agent module responsible for orchestrating irrigation execution based on a schedule.
 * This module listens to time events and calls the advisory and irrigation services.
 */
@file:ApplicationModule(displayName = "Agent Module")
package com.vdzon.irrigation.agent

import org.springframework.modulith.ApplicationModule

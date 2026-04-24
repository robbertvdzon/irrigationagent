/**
 * Agent module responsible for orchestrating irrigation execution based on a schedule.
 * This module triggers execution based on a schedule and calls the advisory and irrigation services.
 */
@file:ApplicationModule(displayName = "Agent Module")
package com.vdzon.irrigation.agent

import org.springframework.modulith.ApplicationModule

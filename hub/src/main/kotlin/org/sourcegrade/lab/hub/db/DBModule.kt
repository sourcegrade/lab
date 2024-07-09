/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.lab.hub.db

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.sourcegrade.lab.hub.db.assignment.AssignmentSnapshot
import org.sourcegrade.lab.hub.db.assignment.DBAssignment
import org.sourcegrade.lab.hub.db.assignment.DBAssignmentRepository
import org.sourcegrade.lab.hub.db.course.CourseSnapshot
import org.sourcegrade.lab.hub.db.course.DBCourse
import org.sourcegrade.lab.hub.db.course.DBCourseRepository
import org.sourcegrade.lab.hub.db.user.DBUser
import org.sourcegrade.lab.hub.db.user.DBUserRepository
import org.sourcegrade.lab.hub.db.user.UserSnapshot
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.AssignmentRepository
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.CourseRepository
import org.sourcegrade.lab.hub.domain.MutableAssignmentRepository
import org.sourcegrade.lab.hub.domain.MutableCourseRepository
import org.sourcegrade.lab.hub.domain.MutableUserRepository
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserRepository

val DBModule = module {

    single<EntityConversionContext<User, DBUser>> {
        EntityConversionContextImpl { user, relations -> UserSnapshot.of(user, relations, get()) }
    }.withOptions {
        named("user")
    }

    single<EntityConversionContext<Assignment, DBAssignment>> {
        EntityConversionContextImpl(AssignmentSnapshot::of)
    }.withOptions {
        named("assignment")
    }

    single<EntityConversionContext<Course, DBCourse>> {
        EntityConversionContextImpl { course, relations -> CourseSnapshot.of(course, relations) }
    }.withOptions {
        named("course")
    }

    single { DBUserRepository(get(), get(named("user"))) }.withOptions {
        bind<UserRepository>()
        bind<MutableUserRepository>()
    }

    single { DBCourseRepository(get(), get(named("course"))) }.withOptions {
        bind<CourseRepository>()
        bind<MutableCourseRepository>()
    }

    single { DBAssignmentRepository(get(), get(named("assignment"))) }.withOptions {
        bind<AssignmentRepository>()
        bind<MutableAssignmentRepository>()
    }
}

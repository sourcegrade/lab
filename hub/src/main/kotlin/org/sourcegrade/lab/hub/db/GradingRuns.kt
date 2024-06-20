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

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.GradingRun
import java.util.UUID
import kotlin.time.Duration

internal object GradingRuns : UUIDTable("sgl_grading_runs") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val submissionId = reference("submission_id", Submissions)
    val gradedRubricId = reference("graded_rubric_id", GradedRubrics)
    val runtime = duration("runtime")

    // duplicated data from GradedRubric to minimize joins for frequent queries
    val rubricId = reference("rubric_id", Rubrics)
    val minPoints = integer("min_points")
    val maxPoints = integer("max_points")
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
}

@GraphQLIgnore
internal class DBGradingRun(id: EntityID<UUID>) : UUIDEntity(id), GradingRun {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by GradingRuns.createdUtc
    override val submission: DBSubmission by DBSubmission referencedOn GradingRuns.submissionId
    override val gradedRubric: DBGradedRubric by DBGradedRubric referencedOn GradingRuns.gradedRubricId
    override val runtime: Duration by GradingRuns.runtime

    override val rubric: DBRubric by DBRubric referencedOn GradingRuns.rubricId
    override val minPoints: Int by GradingRuns.minPoints
    override val maxPoints: Int by GradingRuns.maxPoints
    override val achievedMinPoints: Int by GradingRuns.achievedMinPoints
    override val achievedMaxPoints: Int by GradingRuns.achievedMaxPoints

    companion object : EntityClass<UUID, DBGradingRun>(GradingRuns)
}

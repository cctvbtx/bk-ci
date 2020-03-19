/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.process.dao

import com.tencent.devops.common.api.util.timestampmilli
import com.tencent.devops.model.process.tables.TPipelineStageTag
import com.tencent.devops.model.process.tables.records.TPipelineStageTagRecord
import com.tencent.devops.process.pojo.PipelineStageTag
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.Result
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PipelineStageTagDao {

    fun add(
        dslContext: DSLContext,
        id: String,
        stageTagName: String,
        defaultFlag: Boolean
    ) {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            dslContext.insertInto(
                this,
                ID,
                STAGE_TAG_NAME,
                DEFAULT_FLAG
            )
                .values(
                    id,
                    stageTagName,
                    defaultFlag
                ).execute()
        }
    }

    fun delete(dslContext: DSLContext, id: String) {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            dslContext.deleteFrom(this)
                .where(ID.eq(id))
                .execute()
        }
    }

    fun update(
        dslContext: DSLContext,
        id: String,
        stageTagName: String,
        defaultFlag: Boolean
    ) {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            dslContext.update(this)
                .set(STAGE_TAG_NAME, stageTagName)
                .set(DEFAULT_FLAG, defaultFlag)
                .set(UPDATE_TIME, LocalDateTime.now())
                .where(ID.eq(id))
                .execute()
        }
    }

    fun getStageTag(dslContext: DSLContext, id: String): TPipelineStageTagRecord? {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            return dslContext.selectFrom(this)
                .where(ID.eq(id))
                .fetchOne()
        }
    }

    fun getAllStageTag(dslContext: DSLContext): Result<TPipelineStageTagRecord> {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            return dslContext
                .selectFrom(this)
                .orderBy(CREATE_TIME.desc())
                .fetch()
        }
    }

    fun getDefaultStageTag(dslContext: DSLContext): Result<TPipelineStageTagRecord> {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            return dslContext
                .selectFrom(this)
                .where(DEFAULT_FLAG.eq(true))
                .orderBy(CREATE_TIME.desc())
                .fetch()
        }
    }

    fun convert(record: TPipelineStageTagRecord): PipelineStageTag {
        with(record) {
            return PipelineStageTag(
                id = id,
                stageTagName = stageTagName,
                defaultFlag = defaultFlag,
                createTime = createTime.timestampmilli(),
                updateTime = updateTime.timestampmilli()
            )
        }
    }

    fun countByName(dslContext: DSLContext, stageTagName: String): Record1<Int>? {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            return dslContext.selectCount().from(this).where(STAGE_TAG_NAME.eq(stageTagName)).fetchOne()
        }
    }

    fun countDefaultTag(dslContext: DSLContext): Record1<Int>? {
        with(TPipelineStageTag.T_PIPELINE_STAGE_TAG) {
            return dslContext.selectCount().from(this).where(DEFAULT_FLAG.eq(true)).fetchOne()
        }
    }
}
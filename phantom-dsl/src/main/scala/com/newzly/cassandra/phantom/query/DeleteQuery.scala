/*
 * Copyright 2013 newzly ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com
package newzly
package cassandra
package phantom
package query

import com.datastax.driver.core.querybuilder.{ Clause, Delete }
import com.newzly.cassandra.phantom.{ CassandraTable }

class DeleteQuery[T <: CassandraTable[T, R], R](table: T, val qb: Delete) extends ExecutableStatement {

  def where[RR](c: T => AbstractColumn[RR], value: RR, operator: (T => AbstractColumn[RR],RR)=>T=>Clause): DeleteWhere[T, R] = {
    val clause = operator(c,value.asInstanceOf[RR])(table)
    new DeleteWhere[T, R](table, qb.where(clause))
  }
}

class DeleteWhere[T <: CassandraTable[T, R], R](table: T, val qb: Delete.Where) extends ExecutableStatement {

  def where[RR](c: T => AbstractColumn[RR], value: RR, operator: (T => AbstractColumn[RR],RR)=>T=>Clause): DeleteWhere[T, R] = {
    val clause = operator(c,value.asInstanceOf[RR])(table)
    new DeleteWhere[T, R](table, qb.and(clause))
  }

  def and  = where _
}
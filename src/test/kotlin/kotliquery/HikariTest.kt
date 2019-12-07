package kotliquery

import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HikariTest {

	init {
		HikariCP.default("jdbc:h2:mem:usage;MODE=PostgreSQL", "user", "pass")
	}

	@Test
	fun defaultDataSource1() {
		using(sessionOf(HikariCP.dataSource())) { session ->
			assertNotNull(session)
		}
	}

	@Test
	fun defaultDataSource2() {
		using(sessionOf(HikariCP.dataSource("default"))) { session ->
			assertNotNull(session)
		}
	}

	@Test
	fun undefinedDataSource() {
		assertFailsWith(IllegalStateException::class) {
			using(sessionOf(HikariCP.dataSource("foo"))) { session ->
				assertNotNull(session)
			}
		}
	}

	@Test
	fun initNewDataSource() {
		val ds1 = HikariCP.dataSource("default") // the original DS
		HikariCP.init("default", "jdbc:h2:mem:usage;MODE=PostgreSQL", "user", "pass") // a new DS with the same name
		val ds2 = HikariCP.dataSource("default") // the new DS
		assertNotEquals(ds1, ds2, "ds2 is a new DataSource")

		assertTrue(ds1.isClosed, "the original DS should be closed")
	}

}
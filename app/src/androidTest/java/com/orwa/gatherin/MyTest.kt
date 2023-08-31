package com.orwa.gatherin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class MyTest {

    @Test
    fun testList(){
        val l = ArrayList<Item>()
        l.add(Item(1,"1"))
        l.add(Item(2,"2"))

        val x = Item(2,"2")

        val t = l.get(0)
        t.id=9

        assert(l[0].id!=9)


    }

    data class Item(var id:Int,val name:String)
}
package com.raywenderlich.dailywash.database

object OrderUtil {

    var Orders = arrayOf(
            Order(name = "Siti Nur Reyna", date = "10 Desember 2022" ,type = "Cuci Kering", price = "Rp. 25.000", weight = 1.5, status = "Menunggu"),
            Order(name = "Muhammad Brimstone", date = "12 Desember 2022" ,type = "Cuci Basah", price = "Rp. 35.000", weight = 2.0, status = "Diproses"),
            Order(name = "Ahmad Yoru", date = "13 Desember 2022" ,type = "Cuci Basah", price = "Rp. 35.000", weight = 1.0, status = "Selesai"),
            Order(name = "Nur Jett", date = "13 Desember 2022" ,type = "Cuci Kering", price = "Rp. 35.000", weight = 3.0, status = "Selesai")
    )
}
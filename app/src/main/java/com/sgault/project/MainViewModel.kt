package com.sgault.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    var selectedWebsite = MutableLiveData<DomainInfo>()
    var websites =  mutableListOf<DomainInfo>()
    var domains = mutableListOf<String>()

    fun getRank():Int{
        for((index, website) in websites.withIndex()){
            if(website.domain == selectedWebsite.value!!.domain){
                return index + 1
            }
        }
        return 0
    }

    fun addWebsite(domain:String, pages:String, urls:String, hosts:String, percentPages:String, percentUrls:String) {
       var d:DomainInfo = DomainInfo(domain, pages, urls, hosts, percentPages, percentUrls)
        websites.add(d)
    }
    fun addDomain(domain:String){
        domains.add(domain)
    }
    fun getDomain():String{
        return selectedWebsite.value!!.domain
    }
    fun selectWebsite(domain: String){
        //find the website in the list
        selectedWebsite.value = websites.find { it.domain == domain }


    }

}
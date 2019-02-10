# -*- coding: utf-8 -*-
"""
Created on Sat Feb  9 14:45:28 2019

@author: james
"""
import pandas as pd
import json

df = pd.read_csv("WasteWizard_Item_Description.csv")
a = []
b = []
c = 0
for i,row in df.iterrows():
    d = {}
    d["title"] = row["TITLE"]
    

    d["keywords"] = row["TITLE"] +", "+ str(row["ALT_WORDS"])
    d["description"] = row["DESCRIPTION"]
    a.append(d)
    


string= ""
for i in a:
    
    string += json.dumps(i)
    string += "UNIQUESTRINGSPLIT"


string = string[:-17]


file = open("parsedWizard2.txt","w")
file.write(string)
file.close()

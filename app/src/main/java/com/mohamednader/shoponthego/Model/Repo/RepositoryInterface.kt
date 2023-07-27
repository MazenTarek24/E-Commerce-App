package com.mohamednader.shoponthego.Model.Repo

import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.DataStore.DataStoreSource

interface RepositoryInterface : RemoteSource, LocalSource, DataStoreSource
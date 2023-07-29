package com.mohamednader.shoponthego.Model.Pojo.Products.brand

data class SmartCollection(
        var admin_graphql_api_id: String,
        var body_html: String,
        var disjunctive: Boolean,
        var handle: String,
        var id: Long,
        var image: Image,
        var published_at: String,
        var published_scope: String,
        var rules: List<Rule>,
        var sort_order: String,
        var template_suffix: Any,
        var title: String,
        var updated_at: String
)